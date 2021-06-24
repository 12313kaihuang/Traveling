package com.yu.hu.emoji.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.yu.hu.emoji.R;
import com.yu.hu.emoji.entity.Emoji;
import com.yu.hu.emoji.db.repository.EmojiRepository;
import com.yu.hu.emoji.widget.EmojiRecyclerView;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Hy on 2019/12/27 19:49
 * <p>
 * 表情管理类
 * <p>
 * 建议application初始化时调用{@link #init()}提前进行初始化操作
 *
 * @see #matcher(String)
 * @see #getEmojiRes(String)  获取表情资源id
 * @see #getEmojiText(int)  获取表情对应文字
 * @see #getAllQQEmoji()  获取qq表情
 * @see #getAllEmoji()  获取emoji表情
 **/
@SuppressWarnings({"unused", "WeakerAccess"})
public class EmojiManager {

    private static final String TAG = "EmojiManager";

    /**
     * EmojiRepository
     */
    private static EmojiRepository sEmojiRepository;

    //匹配表情[**\tr]
    private static final String EMOJI_REGEX = "\\[[qe]_[a-z0-9]+\\\\tr]";

    private static final int DEFAULT_RECENT_EMOJI_NUM = EmojiRecyclerView.DEFAULT_SPAN_COUNT * 3;

    /**
     * 用于正则匹配
     * <p>
     * 在此处初始化是为了触发静态代码块
     */
    private static Pattern sPATTERN;

    //默认表情 - 微笑
    private static final int DEFAULT_EMOJI = R.drawable.q_wx;

    //由于static代码块发送在初始化阶段 所以需要调用一个init方法静态代码块才会执行
    public static void init() {
        //执行一次查询操作以触发callback回调
        sEmojiRepository.queryAllByType(Emoji.TYPE_EMOJI);
    }

    //静态代码块执行在类初始化阶段，但是数据库操作是异步的，所以需要提前初始化才行
    static {
        Log.d(TAG, "static initializer: ");
        sEmojiRepository = EmojiRepository.getInstance(getApplicationByReflect());
        sPATTERN = Pattern.compile(EMOJI_REGEX);
    }


    /**
     * 获取所有表情
     *
     * @see com.yu.hu.emoji.db.EmojiDatabase.InitTask
     */
    @SuppressWarnings("JavadocReference")
    public static Emoji[] getAllEmojis() {
        return getAllEmojiList().toArray(new Emoji[0]);
    }

    private static List<Emoji> getAllEmojiList() {
        List<Emoji> emojiList = new ArrayList<>();

        //qq表情
        emojiList.add(new Emoji("[q_wx\\tr]", R.drawable.q_wx, "[微笑]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_qx\\tr]", R.drawable.q_kx, "[苦笑]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_hc\\tr]", R.drawable.q_hc, "[花痴]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_dz\\tr]", R.drawable.q_dz, "[呆滞]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_cool\\tr]", R.drawable.q_cool, "[耍酷]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_dk\\tr]", R.drawable.q_dk, "[大哭]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_hx\\tr]", R.drawable.q_hx, "[害羞]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bz\\tr]", R.drawable.q_bz, "[闭嘴]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_dks\\tr]", R.drawable.q_dks, "[打瞌睡]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_wq\\tr]", R.drawable.q_wq, "[委屈]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_gg\\tr]", R.drawable.q_gg, "[尴尬]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_fn\\tr]", R.drawable.q_fn, "[愤怒]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_tp\\tr]", R.drawable.q_tp, "[调皮]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_zy\\tr]", R.drawable.q_zy, "[龇牙]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_jy\\tr]", R.drawable.q_jy, "[惊讶]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bkx\\tr]", R.drawable.q_bkx, "[不开心]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_cool2\\tr]", R.drawable.q_cool2, "[酷]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_lh\\tr]", R.drawable.q_lh, "[冷汗]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_zk\\tr]", R.drawable.q_zk, "[抓狂]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_ot\\tr]", R.drawable.q_ot, "[呕吐]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_tx\\tr]", R.drawable.q_tx, "[偷笑]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_ka\\tr]", R.drawable.q_ka, "[可爱]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_ws\\tr]", R.drawable.q_ws, "[无视]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_am\\tr]", R.drawable.q_am, "[傲慢]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_tz\\tr]", R.drawable.q_tz, "[馋]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_fk\\tr]", R.drawable.q_fk, "[犯困]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_jk\\tr]", R.drawable.q_jk, "[惊恐]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_liuh\\tr]", R.drawable.q_liuh, "[流汗]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_dx\\tr]", R.drawable.q_dx, "[大笑]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_jm\\tr]", R.drawable.q_jm, "[军帽]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_nl\\tr]", R.drawable.q_nl, "[努力]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_cj\\tr]", R.drawable.q_cj, "[吵架]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_yw\\tr]", R.drawable.q_yw, "[疑问]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_xu\\tr]", R.drawable.q_xu, "[嘘]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_yun\\tr]", R.drawable.q_yun, "[晕]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_zk2\\tr]", R.drawable.q_zk2, "[烦]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_dm\\tr]", R.drawable.q_dm, "[倒霉]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_klt\\tr]", R.drawable.q_klt, "[骷髅头]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_qt\\tr]", R.drawable.q_qt, "[敲头]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_zj\\tr]", R.drawable.q_zj, "[再见]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_ch\\tr]", R.drawable.q_ch, "[擦汗]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_kb\\tr]", R.drawable.q_kb, "[抠鼻]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_gz\\tr]", R.drawable.q_gz, "[鼓掌]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_j\\tr]", R.drawable.q_j, "[囧]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_huaix\\tr]", R.drawable.q_huaix, "[坏笑]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_zhh\\tr]", R.drawable.q_zhh, "[左哼哼]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_yhh\\tr]", R.drawable.q_yhh, "[右哼哼]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_hq\\tr]", R.drawable.q_hq, "[哈欠]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bs\\tr]", R.drawable.q_bs, "[鄙视]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_wq2\\tr]", R.drawable.q_wq2, "[委屈]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_wq3\\tr]", R.drawable.q_wq3, "[委屈]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_hx2\\tr]", R.drawable.q_hx2, "[坏笑]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_qq\\tr]", R.drawable.q_qq, "[亲亲]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_jx\\tr]", R.drawable.q_jx, "[惊吓]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_kl\\tr]", R.drawable.q_kl, "[可怜]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_tp2\\tr]", R.drawable.q_tp2, "[调皮]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_xk\\tr]", R.drawable.q_xk, "[笑哭]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bot\\tr]", R.drawable.q_bot, "[确定?]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_ku\\tr]", R.drawable.q_ku, "[哭]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_wn\\tr]", R.drawable.q_wn, "[无奈]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_ts\\tr]", R.drawable.q_ts, "[托腮]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_mm\\tr]", R.drawable.q_mm, "[卖萌]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_xyx\\tr]", R.drawable.q_xyx, "[斜眼笑]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_px\\tr]", R.drawable.q_px, "[喷血]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_jxi\\tr]", R.drawable.q_jxi, "[惊喜]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_sr\\tr]", R.drawable.q_sr, "[骚扰]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_xjj\\tr]", R.drawable.q_xjj, "[小纠结]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_wzm\\tr]", R.drawable.q_wzm, "[我最美]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_jy2\\tr]", R.drawable.q_jy2, "[加油]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bb2\\tr]", R.drawable.q_bb2, "[抱抱]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_dkz\\tr]", R.drawable.q_dkz, "[戴口罩]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bld\\tr]", R.drawable.bld, "[玩电脑]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_ble\\tr]", R.drawable.ble, "[玩电脑2]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_blf\\tr]", R.drawable.blf, "[捏眼睛]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_blg\\tr]", R.drawable.blg, "[思考]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_blh\\tr]", R.drawable.blh, "[笑哭]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bli\\tr]", R.drawable.bli, "[看手机]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_blj\\tr]", R.drawable.blj, "[哼哼]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_blk\\tr]", R.drawable.blk, "[掉头发]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bll\\tr]", R.drawable.bll, "[???]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_blm\\tr]", R.drawable.blm, "[偷瞄]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bln\\tr]", R.drawable.bln, "[可爱]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bq5\\tr]", R.drawable.bq5, "[吃瓜]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bq6\\tr]", R.drawable.bq6, "[微笑]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_brs\\tr]", R.drawable.brs, "[柠檬精]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_brx\\tr]", R.drawable.brx, "[南风]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_cd\\tr]", R.drawable.q_cd, "[菜刀]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_xg\\tr]", R.drawable.q_xg, "[西瓜]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_pj\\tr]", R.drawable.q_pj, "[啤酒]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_lq\\tr]", R.drawable.q_lq, "[篮球]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_pp\\tr]", R.drawable.q_pp, "[乒乓]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_tea\\tr]", R.drawable.q_tea, "[茶]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_cf\\tr]", R.drawable.q_cf, "[咖啡]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_m\\tr]", R.drawable.q_m, "[米饭]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_pig\\tr]", R.drawable.q_pig, "[猪头]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_h\\tr]", R.drawable.q_h, "[玫瑰花]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_h2\\tr]", R.drawable.q_h2, "[玫瑰花2]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_c\\tr]", R.drawable.q_c, "[红唇]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_f028\\tr]", R.drawable.f028, "[爱心]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_x2\\tr]", R.drawable.q_x2, "[心碎]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bn1\\tr]", R.drawable.bn1, "[蛋糕]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_d\\tr]", R.drawable.q_d, "[闪电]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_f016\\tr]", R.drawable.f016, "[炸弹]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bn7\\tr]", R.drawable.bn7, "[刀]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_biz\\tr]", R.drawable.biz, "[蜡烛]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bna\\tr]", R.drawable.bna, "[足球]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bn3\\tr]", R.drawable.bn3, "[瓢虫]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bm1\\tr]", R.drawable.bm1, "[便便]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_y\\tr]", R.drawable.q_y, "[月亮]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_r\\tr]", R.drawable.q_r, "[太阳]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_g\\tr]", R.drawable.q_g, "[礼物]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bmq\\tr]", R.drawable.bmq, "[抱抱]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_z\\tr]", R.drawable.q_z, "[赞]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bmx\\tr]", R.drawable.bmx, "[踩]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bmy\\tr]", R.drawable.bmy, "[握手]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bmz\\tr]", R.drawable.bmz, "[yean]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bn0\\tr]", R.drawable.bn0, "[抱拳]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bn4\\tr]", R.drawable.bn4, "[勾引]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bn9\\tr]", R.drawable.bn9, "[拳]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bn8\\tr]", R.drawable.bn8, "[low]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bn5\\tr]", R.drawable.bn5, "[爱]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bnp\\tr]", R.drawable.bnp, "[no]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_o\\tr]", R.drawable.q_o, "[ok]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bmk\\tr]", R.drawable.bmk, "[爱]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_f047\\tr]", R.drawable.f047, "[企鹅_爱心]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bnq\\tr]", R.drawable.bnq, "[企鹅_晃]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_f071\\tr]", R.drawable.f071, "[企鹅_倒]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bnr\\tr]", R.drawable.bnr, "[企鹅_生气]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bns\\tr]", R.drawable.bns, "[企鹅_滑]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bnt\\tr]", R.drawable.bnt, "[企鹅]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bnu\\tr]", R.drawable.bnu, "[企鹅]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bnv\\tr]", R.drawable.bnv, "[企鹅_跳绳]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bnb\\tr]", R.drawable.bnb, "[企鹅_投降]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bnw\\tr]", R.drawable.bnw, "[企鹅_汉]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bnx\\tr]", R.drawable.bnx, "[企鹅]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bny\\tr]", R.drawable.bny, "[企鹅]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bnz\\tr]", R.drawable.bnz, "[企鹅]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bo0\\tr]", R.drawable.bo0, "[企鹅]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_biq\\tr]", R.drawable.biq, "[企鹅]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bir\\tr]", R.drawable.bir, "[囍]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bis\\tr]", R.drawable.bis, "[灯笼]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_biu\\tr]", R.drawable.biu, "[麦克风]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_biy\\tr]", R.drawable.biy, "[庆祝]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bj0\\tr]", R.drawable.bj0, "[爆筋]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bj1\\tr]", R.drawable.bj1, "[棒棒糖]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bj2\\tr]", R.drawable.bj2, "[奶瓶]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bj5\\tr]", R.drawable.bj5, "[飞机]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bjb\\tr]", R.drawable.bjb, "[钞票]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bjl\\tr]", R.drawable.bjl, "[药]", Emoji.TYPE_QQ));
        emojiList.add(new Emoji("[q_bjm\\tr]", R.drawable.bjm, "[左轮]", Emoji.TYPE_QQ));

        //emoji
        emojiList.add(new Emoji("[e_kx\\tr]", R.drawable.e_kx, "[开心]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_yy\\tr]", R.drawable.e_yy, "[愉悦]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_qin\\tr]", R.drawable.e_qin, "[亲亲]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_han\\tr]", R.drawable.e_han, "[汗]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_ns\\tr]", R.drawable.e_ns, "[难受]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_ts\\tr]", R.drawable.e_ts, "[吐舌头]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_hei\\tr]", R.drawable.e_hei, "[嘻嘻]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_wp\\tr]", R.drawable.e_wp, "[顽皮]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_sf\\tr]", R.drawable.e_sf, "[舒服]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_nd\\tr]", R.drawable.e_nd, "[你懂]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_xh\\tr]", R.drawable.e_xh, "[喜欢]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_sad\\tr]", R.drawable.e_sad, "[伤心]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_lau\\tr]", R.drawable.e_lau, "[开心]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_xx\\tr]", R.drawable.e_xx, "[邪笑]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_x\\tr]", R.drawable.e_x, "[笑]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_dai\\tr]", R.drawable.e_dai, "[瞪眼]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_an\\tr]", R.drawable.e_an, "[爱你]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_ku\\tr]", R.drawable.e_ku, "[哭]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_kp\\tr]", R.drawable.e_kp, "[可怕]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_xk\\tr]", R.drawable.e_xk, "[笑哭]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_jy\\tr]", R.drawable.e_jy, "[加油]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_q\\tr]", R.drawable.e_q, "[拳]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_zan\\tr]", R.drawable.e_zan, "[赞]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_no\\tr]", R.drawable.e_no, "[no]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_gz\\tr]", R.drawable.e_gz, "[鼓掌]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_yean\\tr]", R.drawable.e_yean, "[yean]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_cai\\tr]", R.drawable.e_cai, "[踩]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_qd\\tr]", R.drawable.e_qd, "[祈祷]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_ok\\tr]", R.drawable.e_ok, "[ok]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_left\\tr]", R.drawable.e_left, "[左]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_right\\tr]", R.drawable.e_right, "[右]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_top\\tr]", R.drawable.e_top, "[上]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bottom\\tr]", R.drawable.e_bottom, "[下]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_eye\\tr]", R.drawable.e_eye, "[眼睛]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_ba0\\tr]", R.drawable.ba0, "[鼻子]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_ba1\\tr]", R.drawable.ba1, "[嘴唇]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_ba2\\tr]", R.drawable.ba2, "[耳朵]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_ba3\\tr]", R.drawable.ba3, "[米饭]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_ba4\\tr]", R.drawable.ba4, "[意大利面]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_ba5\\tr]", R.drawable.ba5, "[面条]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_ba6\\tr]", R.drawable.ba6, "[饭团]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_ba7\\tr]", R.drawable.ba7, "[冰淇淋]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_ba8\\tr]", R.drawable.ba8, "[面包]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_ba9\\tr]", R.drawable.ba9, "[蛋糕]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bea\\tr]", R.drawable.ba_, "[吐司]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_baa\\tr]", R.drawable.baa, "[汉堡]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bab\\tr]", R.drawable.bab, "[煎蛋]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bac\\tr]", R.drawable.bac, "[薯条]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bad\\tr]", R.drawable.bad, "[啤酒]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bae\\tr]", R.drawable.bae, "[啤酒2]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_baf\\tr]", R.drawable.baf, "[冰沙]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bag\\tr]", R.drawable.bag, "[咖啡]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bah\\tr]", R.drawable.bah, "[苹果]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bai\\tr]", R.drawable.bai, "[橘子]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_baj\\tr]", R.drawable.baj, "[草莓]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bak\\tr]", R.drawable.bak, "[西瓜]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bal\\tr]", R.drawable.bal, "[药]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bam\\tr]", R.drawable.bam, "[烟]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_ban\\tr]", R.drawable.ban, "[松树]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bao\\tr]", R.drawable.bao, "[玫瑰花]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bap\\tr]", R.drawable.bap, "[庆祝]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_baq\\tr]", R.drawable.baq, "[树]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bar\\tr]", R.drawable.bar, "[礼盒]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bas\\tr]", R.drawable.bas, "[蝴蝶结]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bat\\tr]", R.drawable.bat, "[气球]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bau\\tr]", R.drawable.bau, "[海螺]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bav\\tr]", R.drawable.bav, "[戒指]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_baw\\tr]", R.drawable.baw, "[炸弹]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bax\\tr]", R.drawable.bax, "[皇冠]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bay\\tr]", R.drawable.bay, "[铃铛]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_baz\\tr]", R.drawable.baz, "[星星]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bb0\\tr]", R.drawable.bb0, "[星星2]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bb1\\tr]", R.drawable.bb1, "[空气]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bb2\\tr]", R.drawable.bb2, "[水]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bb3\\tr]", R.drawable.bb3, "[火苗]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bb4\\tr]", R.drawable.bb4, "[奖杯]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bb5\\tr]", R.drawable.bb5, "[钱]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bb6\\tr]", R.drawable.bb6, "[睡觉]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bb7\\tr]", R.drawable.bb7, "[闪电]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bb8\\tr]", R.drawable.bb8, "[脚印]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bb0\\tr]", R.drawable.bb9, "[屎]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bbzh\\tr]", R.drawable.bb_, "[针]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bba\\tr]", R.drawable.bba, "[Java]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bbb\\tr]", R.drawable.bbb, "[邮箱]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bbc\\tr]", R.drawable.bbc, "[钥匙]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bbd\\tr]", R.drawable.bbd, "[锁]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bbe\\tr]", R.drawable.bbe, "[飞机]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bbf\\tr]", R.drawable.bbf, "[高铁]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bbg\\tr]", R.drawable.bbg, "[汽车]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bbh\\tr]", R.drawable.bbh, "[快艇]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bbi\\tr]", R.drawable.bbi, "[自行车]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bbj\\tr]", R.drawable.bbj, "[马]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bbk\\tr]", R.drawable.bbk, "[火箭]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bbl\\tr]", R.drawable.bbl, "[公交车]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bbm\\tr]", R.drawable.bbm, "[帆船]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bbn\\tr]", R.drawable.bbn, "[阿姨]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bbo\\tr]", R.drawable.bbo, "[爷爷]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bbp\\tr]", R.drawable.bbp, "[妈妈]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bbq\\tr]", R.drawable.bbq, "[爸爸]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bbr\\tr]", R.drawable.bbr, "[猴子]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bbs\\tr]", R.drawable.bbs, "[章鱼]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bbt\\tr]", R.drawable.bbt, "[猪]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bbu\\tr]", R.drawable.bbu, "[骷髅]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bbv\\tr]", R.drawable.bbv, "[小鸡]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bbw\\tr]", R.drawable.bbw, "[考拉]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bbx\\tr]", R.drawable.bbx, "[牛]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bby\\tr]", R.drawable.bby, "[鸡]", Emoji.TYPE_EMOJI));
        emojiList.add(new Emoji("[e_bbz\\tr]", R.drawable.bbz, "[青蛙]", Emoji.TYPE_EMOJI));

        return emojiList;
    }

    /**
     * 匹配表情文字
     *
     * @param content content
     */
    public static Matcher matcher(String content) {
        return sPATTERN.matcher(content);
    }

    /**
     * 获取表情res
     * 若搜索不到则返回默认表情{@link #DEFAULT_EMOJI}
     */
    public static int getEmojiRes(String emoji) {
        int res = sEmojiRepository.getEmojiRes(emoji);
        return res == 0 ? DEFAULT_EMOJI : res;
    }

    /**
     * 获取表情对应的Text
     * 若搜索不到则返回空串
     */
    @NonNull
    public static String getEmojiText(int emojiRes) {
        String emojiText = sEmojiRepository.getEmojiText(emojiRes);
        return emojiText == null ? "" : emojiText;
    }

    /**
     * @return 所有qq表情
     */
    public static List<Emoji> getAllQQEmoji() {
        return sEmojiRepository.queryAllByType(Emoji.TYPE_QQ);
    }

    /**
     * @return 所有emoji表情
     */
    public static List<Emoji> getAllEmoji() {
        return sEmojiRepository.queryAllByType(Emoji.TYPE_EMOJI);
    }

    /**
     * 获取最近使用的表情
     */
    @NonNull
    public static List<Emoji> getRecentEmoji() {
        return getRecentEmoji(DEFAULT_RECENT_EMOJI_NUM);
    }

    /**
     * 获取最近使用的表情
     *
     * @param num 表情数
     */
    @NonNull
    public static List<Emoji> getRecentEmoji(int num) {
        return sEmojiRepository.getRecentEmoji(num);
    }

    /**
     * 记录最近点击时间
     *
     * @param emoji emoji
     */
    public static void recentClick(Emoji emoji) {
        emoji.recent();
        sEmojiRepository.insertAll(emoji);
    }

    /**
     * 通过反射获取到Application
     *
     * @return Application
     */
    private static Application getApplicationByReflect() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(thread);
            if (app == null) {
                throw new NullPointerException("u should init first");
            }
            return (Application) app;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("u should init first");
    }

}
