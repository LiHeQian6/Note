package com.zhifou.note.note.controller;

import com.zhifou.note.annotation.WordFilter;
import com.zhifou.note.bean.Constant;
import com.zhifou.note.bean.NoteVO;
import com.zhifou.note.exception.NoteException;
import com.zhifou.note.exception.UserException;
import com.zhifou.note.message.service.CollectService;
import com.zhifou.note.message.service.FollowService;
import com.zhifou.note.message.service.LikeService;
import com.zhifou.note.message.service.LookService;
import com.zhifou.note.note.entity.Note;
import com.zhifou.note.note.service.CommentService;
import com.zhifou.note.note.service.NoteService;
import com.zhifou.note.user.entity.User;
import com.zhifou.note.user.service.DataService;
import com.zhifou.note.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : li
 * @Date: 2021-02-08 17:23
 */
@Api("笔记模块")
@RestController
public class NoteController implements Constant {
    @Resource
    private NoteService noteService;
    @Resource
    private LookService lookService;
    @Resource
    private LikeService likeService;
    @Resource
    private CommentService commentService;
    @Resource
    private FollowService followService;
    @Resource
    private CollectService collectService;
    @Resource
    private DataService dataService;
    @Resource
    private JwtUtils jwtUtils;

    @ApiOperation("浏览笔记")
    @GetMapping("/note/{id}")
    public NoteVO browseNote(@PathVariable int id, HttpServletRequest request) throws NoteException {
        User userInfo = jwtUtils.getUserInfo();
        lookService.look(request.getRemoteAddr(),id);
        Note note = noteService.getNote(id);
        NoteVO noteVO = noteService.getNoteData(note);
        if (userInfo !=null) {
            dataService.recordDAU(userInfo.getId());
            noteVO.getUser().setFollow(followService.hasFollowed(userInfo.getId(), note.getUser().getId()));
            noteVO.getUser().setFollower(followService.getFollowerCount(noteVO.getUser().getId()));
            noteVO.getUser().setFollowee(followService.getFolloweeCount(noteVO.getUser().getId()));
            noteVO.getUser().setLike(likeService.findUserLikeCount(noteVO.getUser().getId()));
            noteVO.setLiked(likeService.findEntityLikeStatus(userInfo.getId(),ENTITY_TYPE_NOTE, id));
            noteVO.setCollected(collectService.hasCollected(userInfo.getId(),id));
        }
        return noteVO;
    }

    @WordFilter(description = "note")
    @ApiOperation("发布笔记")
    @PostMapping("/note")
    public void publishNote(@ApiParam("只需要传title,content,type.id,tags=[tag.id]") @Valid @RequestBody Note note) throws Exception {
        User userInfo = jwtUtils.getUserInfo();
        note.setUser(userInfo);
        noteService.addNote(note);
    }

    @WordFilter(description = "note")
    @ApiOperation("修改笔记")
    @PutMapping("/note")
    public void editNote(@ApiParam("只需要传id,title,content,type.id,tags=[tag.id]") @Valid @RequestBody Note newNote) throws NoteException, IOException {
        User userInfo = jwtUtils.getUserInfo();
        Note note = noteService.getNote(newNote.getId(),userInfo.getUsername());
        if (note.update(newNote)) {
            noteService.updateNote(note);
        }
    }

    @ApiOperation("删除笔记")
    @DeleteMapping("/note/{id}")
    public void deleteNote(@PathVariable int id) throws NoteException, IOException {
        User userInfo = jwtUtils.getUserInfo();
        noteService.deleteNote(id,userInfo.getUsername());
    }


    @ApiOperation("根据类型分页获取笔记")
    @GetMapping("/notes/type/{typeId}")
    public Page<NoteVO> getNoteByType(@ApiParam("第几页") @Min(value = 0, message = "页数最小为0") int page,
                                      @ApiParam("页大小") @Min(value = 1, message = "页尺寸最小为1") int size,
                                      @PathVariable int typeId){
        return noteService.getNotesByType(page,size,typeId);
    }

    @ApiOperation("根据标签分页获取笔记")
    @GetMapping("/notes/tag/{tagId}")
    public Page<NoteVO> getNoteByTag(@ApiParam("第几页") @Min(value = 0, message = "页数最小为0") int page,
                                      @ApiParam("页大小") @Min(value = 1, message = "页尺寸最小为1") int size,
                                      @PathVariable int tagId){
        return noteService.getNotesByTag(page,size,tagId);
    }

    @ApiOperation("根据热度分页获取笔记")
    @GetMapping("/notes/popularity")
    public Page<NoteVO> getNoteByPopularity(@ApiParam("第几页") @Min(value = 0, message = "页数最小为0") int page,
                                      @ApiParam("页大小") @Min(value = 1, message = "页尺寸最小为1") int size) throws ParseException {
        Page<NoteVO> notes = noteService.getNotesByPage(page, size);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, Calendar.FEBRUARY,1);
        //热度=点赞*4+收藏*6+日期%2021-1-1
        for (NoteVO note : notes.getContent()) {
            long like = likeService.findEntityLikeCount(ENTITY_TYPE_NOTE, note.getId());
            long collect = collectService.getNoteCollectCount(note.getId());
            long look = lookService.lookNum(note.getId());
            Date createTime = format.parse(note.getCreateTime());
            note.setPopularity((long) (look+like*4+collect*6+ createTime.getTime()%calendar.getTime().getTime()));
        }
        ArrayList<NoteVO> noteVOList = new ArrayList<>(notes.getContent());
        PageRequest pageRequest = PageRequest.of(page, size);
        noteVOList.sort(new Comparator<NoteVO>() {
            @Override
            public int compare(NoteVO o1, NoteVO o2) {
                return Integer.valueOf(String.valueOf(o1.getPopularity() - o2.getPopularity()));
            }
        });

        return new PageImpl<>(noteVOList, pageRequest, notes.getTotalElements());
    }

    @ApiOperation("获取用户笔记列表")
    @GetMapping("/notes/user/{userId}")
    public Page<NoteVO> getNotesByUserId(@PathVariable int userId,
                                         @ApiParam("第几页") @Min(value = 0, message = "页数最小为0") int page,
                                        @ApiParam("页大小") @Min(value = 1, message = "页尺寸最小为1") int size) throws UserException {
        return noteService.getNotesByUser(userId,page,size);
    }

    @ApiOperation("获取用户热门或最新笔记列表")
    @GetMapping("/notes/user/type/{userId}/{type}")
    public Page<NoteVO> getNotesByUserIdAndType(@PathVariable int userId,
                                        @ApiParam("最新0，热门1") @PathVariable int type,
                                        @ApiParam("第几页") @Min(value = 0, message = "页数最小为0") int page,
                                        @ApiParam("页大小") @Min(value = 1, message = "页尺寸最小为1") int size) throws UserException {
        return noteService.getNotesByUserAndType(userId,page,size,type);
    }


    @ApiOperation("获取用户关注的作者的笔记")
    @GetMapping("/notes/followers")
    public Page<NoteVO> getUserFollowersNotes(@ApiParam("第几页") @Min(value = 0, message = "页数最小为0") int page,
                                              @ApiParam("页大小") @Min(value = 1, message = "页尺寸最小为1") int size){
        User userInfo = jwtUtils.getUserInfo();
        return noteService.getUserFollowersNotes(userInfo.getId(),page,size);
    }

}
