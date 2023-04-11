 package com.Server.ninja.Server;

public class Text {
    private static final String[] TEXTVIE;

    public static String get(final int type, final int num) {
        try {
            if (type == 0) {
                return Text.TEXTVIE[num];
            }
        } catch (Exception ex) {
        }
        return String.format("ERROR[%d:%d]", type, num);
    }

    static {
        TEXTVIE = new String[]{
                "Thông tin tài khoản hoặc mật khẩu không chính xác",//0
                "Tài khoản này hiện đang bị khóa. Để biết thêm thông tin chi tiết hãy liên hệ ADMIN.",//1
                "Có người đăng nhập vào tài khoản của bạn",//2
                "Bạn đang đăng nhập tại một thiết bị khác, hãy thử đăng nhập lại sau vài phút",//3
                "Chỉ có thể tạo tối đa %d nhân vật!",//4
                "Tên nhân chỉ đồng ý các ký tự a-z,0-9 và chiều dài từ 5 đến 15 ký tự",//5
                "Tên nhân vật đã tồn tại",//6
                "Vui lòng chờ sau %s giây",//7
                "Bạn không có đủ lượng trên người.",//8
                "Khu vực quá tải, vui lòng quay lại sau.",//9
                "Nhận",//10
                "Không",//11
                "Hoàn thành nhiệm vụ",//12
                "Việc đầu tiên con cần là đi nói chuyện với những người trong làng. Con đồng ý chứ?",//13
                "Chào con, ta là Kiriko, chuyên bán dược phẩm chữa trị vết thương",//14
                "Hành trang không đủ chỗ trống",//15
                "Lỗi vật phẩm",//16
                "Số lượng không hợp lệ",//17
                "Phải làm xong nhiệm vụ lần đầu dùng kiếm mới có thể bán vật phẩm",//18
                "Hiện tại người chơi đã offline",//19
                "%s đang đứng nhìn bạn",//20
                "Bạn cần phải tháo trang bị thú cưỡi đang sử dụng",//21
                "Bạn cần có thú cưỡi để sử dụng",//22
                "Cần phải tháo hết trang bị thú cưỡi ra trước",//23
                "Bản đồ hiện tại đang quái tải vui lòng quay lại sau giây lát",//24
                "Vật phẩm của người khác",//25
                "Không đủ Yên",//26
                "Không đủ xu",//27
                "Không đủ lượng",//28
                "Phải làm xong nhiệm vụ diệt trừ cóc mới có thể mua vật phẩm",//29
                "Bạn đang có thức ăn cao cấp hơn.",//30
                "Bạn đã có yêu cầu tỷ thí từ trước. Vui lòng chờ cho đến khi yêu cầu tỷ thí được kết thúc",//31
                "Vũ khí không thích hợp",//32
                "Trình độ của bạn chưa đạt yêu cầu",//33
                "Giới tính không phù hợp",//34
                "Bạn chưa có vũ khí. Xin vào Menu/Bản Thân/Trang bị để kiểm tra",//35
                "Rương đồ không đủ chỗ trống",//36
                "Bạn chỉ được phép cất thêm %d xu",//37
                "Bạn chỉ được phép rút thêm %d xu",//38
                "Tốt lắm, ngươi đã chọn nơi này làm nơi trở về khi bị trọng thương",//39
                "Hiện tại người chơi này không online.",//40
                "%s đã có tên trong danh sách bạn bè hoặc thù địch.",//41
                "Đã tối đa danh sách bạn bè",//42
                "Khoảng cách quá xa",//43
                "Bạn đã gởi yêu cầu giao dịch. Sau 30 giây nữa mới được gửi tiếp",//44
                "Điểm hiếu chiến quá cao không thể thay đổi trang thái.",//45
                "Không thể chiến đấu ở khu vực này",//46
                "Điểm hiếu chiến quá cao không thể cừu sát người khác.",//47
                "Bạn đang cừu sát người khác không thể cùng lúc cừu sát nhiều người",//48
                "Điểm hiếu chiến quá cao không thể dùng vật phẩm này.",//49
                "Cần 1 lượng để hồi sinh tại chỗ.",//50
                "Mã quà tặng",//51
                "Mã quà tặng không hợp lệ",//52
                "Không thể cất vật phẩm nhiệm vụ",//53
                "HP đã đầy",//54
                "MP đã đầy",//55
                "Chưa có thông tin",//56
                "%s xu",//57
                "Đã khóa đặt cược",//58
                "Số xu tối đa có thể đặt cược là %s xu",//59
                "Số xu tối thiểu phải là %s xu",//60
                "Số xu tối thiểu phải là %s xu mới có thể tham gia vòng vip",//61
                "Không đủ xu để đặt cược",//62
                "Chúc mừng %s đã chiến thắng %s xu trong trò chơi Vòng xoay may mắn",//63
                "Người vừa chiến thắng:"
                        + "\n%s %s"
                        + "\nSố xu thắng: %s xu"
                        + "\nSố xu tham gia: %s xu",//64
                "Số xu tham gia đã quá tải",//65
                "Vòng xoay vip",//66
                "Vòng xoay thường",//67
                "Cần có phiếu may mắn.",//68
                "- Giá trị nhập xu thấp nhất của mỗi người là 1.000.000"
                        + "\n- Giá trị nhập xu cao nhất của mỗi người là 50.000.000"
                        + "\n- Giá trị còn lại sau mỗi lần đặt phải còn ít nhất 10.000.000"
                        + "\n- Mỗi 2 phút là bắt đầu vòng quay một lần."
                        + "\n- Khi có người bắt đầu nhập xu thì mới bắt đầu đếm ngược thời gian."
                        + "\n- Còn 10 giây cuối sẽ bắt đầu khóa cho gửi xu."
                        + "\n- Người chiến thắng sẽ nhận hết tổng số tiền tất cả người chơi khác đặt sau khi đã trừ thuế."
                        + "\n- Khi người chơi ít hơn 10, thuế sẽ bằng số người chơi -1."
                        + "\n- Người chơi nhiều hơn 10 người thế sẽ là 10%.",//69
                "- Giá trị nhập xu thấp nhất của mỗi người là 10.000"
                        + "\n- Giá trị nhập xu cao nhất của mỗi người là 100.000"
                        + "\n- Mỗi 2 phút là bắt đầu vòng quay một lần."
                        + "\n- Khi có người bắt đầu nhập xu thì mới bắt đầu đếm ngược thời gian."
                        + "\n- Còn 10 giây cuối sẽ bắt đầu khóa cho gửi xu."
                        + "\n- Người chiến thắng sẽ nhận hết tổng số tiền tất cả người chơi khác đặt sau khi đã trừ thuế."
                        + "\n- Khi người chơi ít hơn 10, thuế sẽ bằng số người chơi -1."
                        + "\n- Người chơi nhiều hơn 10 người thế sẽ là 10%.",//70
                "Thông tin",//71
                "Luật chơi",//72
                "Bạn nhận được %d yên",//73
                "%s tham gia thẻ bài bí mật nhận được %s",//74
                "%s tham gia thẻ bài bí mật nhận được %d %s",//75
                "Bỏ Trang bị và Đá vào trong khung để nâng cấp. Khi nâng cấp cẩn thận thì cần phải có lượng.",//76
                "Chỉ được chọn Đá và Bảo hiểm để nâng cấp",//77
                "Bảo hiểm không phù hợp cho cấp của trang bị",//78
                "Tỉ lệ thành công từ 40% trở lên thì mới được luyện.",//79
                "Chỉ được chuyển hóa trang bị cùng loại và cùng cấp trở lên.",//80
                "Cần có vật phẩm Hoán Chuyển theo đúng cấp trang bị.",//81
                "Bạn đã sử dụng túi này rồi. Mỗi người chỉ được sử dụng 1 lần.",//82
                "Cần sử dụng %s mới có thể dùng %s.",//83
                "Bạn chưa thể đi đến khu vực này. Hãy hoàn thành nhiệm vụ trước.",//84
                "Con vẫn chưa đủ điều kiện để vào lớp (trình độ từ cấp 10 và làm xong nhiệm vụ tìm hiểu trường",//85
                "Con đã vào lớp từ trước rồi mà!",//86
                "Con hãy gỡ bỏ tạp niệm bằng cách cất vũ khí đang sử dụng vào hành trang trước rồi hãy vào lớp học của ta.",//87
                "Chào mừng con đến với trường Haruna. Con hãy sử dụng vũ khí và đọc sách võ công mà ta tặng (mở Menu/Bản thân/Hành trang) để bước đầu chuẩn bị cho việc học tập",//88
                "Bạn đã học kĩ năng này rồi",//89
                "Môn phái không phù hợp",//90
                "Không được tăng quá điểm tối đa",//91
                "Trình độ của bạn chưa đủ để nâng cấp",//92
                "Không đủ điểm để nâng cấp",//93
                "Khổng thể nâng cấp kĩ năng này",//94
                "Hành trang con phải tối thiểu có %d ô.",//95
                "Đây là vật phẩm có giá trị, không thể bán.",//96
                "Không thể bán trang bị đã nâng cấp",//97
                "Lời mời tổ đội đã được gởi đi, đang chờ đối phương chấp nhận",//98
                "Bạn đang trong này.",//99
                "Bạn đang trong nhóm khác, không thể xin gia nhập.",//100
                "Bạn đã gởi yêu cầu xin vào nhóm rồi. Xin đừng gởi liên tục.",//101
                "Bạn không phải là đội trưởng.",//102
                "Nhóm đối phương đã đầy",//103
                "Hiện tại người chơi này không online.",//104
                "Đối phương đang ở trong nhóm khác.",//105
                "%s đang là đồng đội của bạn.",//106
                "Nhóm đã đầy",//107
                "Bạn đang trong nhóm khác, không thể chấp nhận vào nhóm này.",//108
                "%s đã được lên làm nhóm trưởng.",//109
                "%s đã rời khỏi nhóm.",//110
                "%s đã bị trục xuất khỏi nhóm.",//111
                "Bạn đã bị trục xuất khỏi nhóm.",//112
                "Nhóm không còn tồn tại.",//113
                "Số nhóm trong khu vực này đã đạt tối đa.",//114
                "Nhóm đã được khóa.",//115
                "Khu vực này đã đầy.",//116
                "Đối phương quá mạnh không thể dùng tà thuật lên mục tiêu",//117
                "Cần phải có khoảng trống phía trước mới có thể thi chiển được chiêu này.",//118
                "Con không phải học sinh trường này, không thể tẩy điểm ở đây.",//119
                "Số lần tẩy điểm tiềm năng của con đã hết.",//120
                "Số lần tẩy điểm kĩ năng của con đã hết.",//121
                "Ta đã giúp con tẩy điểm tiềm năng. Đây là lần cuối con được tẩy tiềm năng, hãy sử dụng cho tốt điểm tiềm năng nhé.",//122
                "Ta đã giúp con tẩy điểm kĩ năng. Đây là lần cuối con được tẩy kĩ năng, hãy sử dụng cho tốt điểm kĩ năng nhé.",//123
                "Số lần tẩy điểm tiềm năng của bạn là %d",//124
                "Số lần tẩy điểm kĩ năng của bạn là %d",//125
                "Sử dụng vật phẩm Thí luyện thiếp để có thể tham gia phiêu lưu tại Vùng Đất Ma Quỷ. Thí luyện thiếp được bán tại NPC Goosho. Nhẫn thuật 80 sẽ xuất hiện tại Vùng Đất Ma Quỷ.",//126
                "Trình độ 60 mới có thể sử dụng chức năng này",//127
                "Trang bị này chỉ dụng cho Xe máy",//128
                "Trang bị này chỉ dụng cho Thú cưỡi",//129
                "Hướng dẫn",//130
                "- Tinh luyện trang b"
                        + "\n- Để tinh luyện trang bị cần phải có Tử tinh thạch."
                        + "\n- Độ tinh luyện cấp 1, 2, 3 phải sử dụng Tử tinh thạch sơ cấp."
                        + "\n- Độ tinh luyện cấp 4, 5, 6 phải sử dụng Tử tinh thạch trung cấp."
                        + "\n- Độ tinh luyện cấp 7, 8, 9 phải sử dụng Tử tinh thạch cao cấp.Tinh luyện đá:"
                        + "\n- Ghép 9 Tử tinh thạch sơ cấp sẽ nhận được 1 Tử tinh thạch trung cấp."
                        + "\n- Ghép 9 Tử tinh thạch trung cấp sẽ nhận được 1 Tử tinh thạch cao cấp."
                        + "\n- Sử dụng 3 viên đá Tử tinh thạch sơ cấp và Đá 11 sẽ nhận được 3 viện tử tinh thạch trung cấp."
                        + "\n-  Sử dụng 3 viên đá Tử tinh thạch trung cấp và Đá 12 sẽ nhận được 3 viên Tử tinh thạch cao cấp."
                        + "\nDịch chuyển trang bị:"
                        + "\n- Trang bị dịch chuyển phải là trang bị +12 trở lên."
                        + "\n- Trang bị sau khi dịch chuyển sẽ có thêm 2 dòng thuộc tính dùng để Tinh luyện."
                        + "\n-Để dịch chuyển cần phải có đủ 20 chuyển tinh thạch."
                        + "\nThăng cấp sao thú cưỡi:"
                        + "\n- Thú cưới đạt level 100 người chơi có thể lựa chọn thăng cấp sao."
                        + "\n- Thăng sao thành công Thú cưỡi sẽ trở lại level 1 với tiềm năng tốt hơn."
                        + "\n- Cần sử dụng Chuyển tinh thạch để tăng sao cho thú cưỡi.",//131
                "Trang bị đã được dịch chuyển.",//132
                "Trang bị này không dùng trong dịch chuyển.",//133
                "Trang bị này không dùng trong tinh luyện.",//134
                "Trang bị đã tinh luyện tối đa.",//135
                "Tinh luyện thất bại.",//136
                "Vật phẩm đã được tinh luyện thành công.",//137
                "Không đủ yên.",//138
                "Không đủ điều khiện để tinh luyện vật phẩm.",//139
                "Tinh Thạch đã được luyện thành công.",//140
                "Tinh Thạch đã được luyện thất bại.",//141
                "Mã quà tặng không tồn tại hoặc đã được sử dụng",//142
                "Có lỗi sảy ra  hãy báo ngay cho Admin để khắc phục.",//143
                "Số lượng mã quà tặng này đã hết.",//144
                "Mỗi tài khoản chỉ được phép sử dụng mã quà tặng này 1 lần duy nhất.",//145
                "Phần quà của bạn là: ",//146
                " yên",//147
                " xu",//148
                " lượng",//149
                " Kinh nghiệm",//150
                "Hành trang phải có ít nhất %d ô trống để nhận vật phẩm.",//151
                "Thần thú đẫ suất hiện tại %s",//152
                "%s đã tiêu diệt %s.",//153
                "Trong người con không có đủ 50 lượng. Khi nào đem đủ lượng thì đến đây gặp ta.",//154
                "Nói chuyện",//155
                "%d. %s có %s yên",//156
                "%d. %s trình độ cấp %d vào ngày %s",//157
                "%d. Gia tộc %s trình độ cấp %d do %s làm tộc trưởng, thành viên %d/%d",//158
                "%d. %s nhận được %d %s",//159
                "Top đại gia",//160
                "Top cao thủ",//161
                "Top Gia Tộc",//162
                "Top hang động",//163
                "Con đã nhận phần thưởng này rồi. Mỗi người chỉ được nhận 1 lần.",//164
                "Trình độ của con không đủ để nhận thưởng.",//165
                "Sự kiện này đã kết thúc không còn sử dụng được vật phẩm này nữa.",//166
                "Hành trang phải còn dư ít nhất %d ô trống để nhận vật phẩm của ta.",//167
                "Chỉ sử dụng tối đa %d cái %s",//168
                "Sự kiện này đã kết thúc. Con không thể sử dụng chức năng này.",//169
                "Hành trang con không có đủ nguyên liệu.",//170
                "Con không có đủ %d yên. Khi nào có đủ hãy quay lại đây gặp ta.",//171
                "%s sử dụng %s nhận được %s",//172
                "Phải vào lớp mới có thể sử dụng vật phẩm này.",//173
                "Cần phải có Hộp diêm để thắp sáng lồng đèn. Hộp diêm có bán tại Goosho.",//174
                "Vật phẩm chỉ sử dụng được khi có thú cưỡi.",//175
                "Vật phẩm chỉ sử dụng được khi có xe máy.",//176
                "Thú cưỡi đã đạt cấp độ tối đa.",//177
                "Chỉ có thể sử dụng vật phẩm này khi thú cưỡi đã đạt cấp độ tối đa.",//178
                "Thú cưỡi đã được tăng sao.",//179
                "Nâng cấp sao thú cưỡi thất bại, bạn bị mất 1 chuyển tinh thạch.",//180
                "Thú cưỡi đã hết hạn vui cất thú cưỡi trước khi chuyển map.",//181
                "Bạn nhận được %d điểm kĩ năng",//182
                "Bạn nhận được %d điểm tiềm năng",//183
                "Vui lòng tải bản %s để sử dụng vật phẩm này",//184
                "Top",//185
                "%d. %s",//186
                "Con đã có gia tộc rồi, không thể lập thêm gia tộc nữa.",//187
                "Hành trang phải có ít nhất %s lượng thì mới có thể lập gia tộc.",//188
                "Trình độ của con phải đạt cấp %d mới có thể lập gia tộc.",//189
                "Tên Gia Tộc",//190
                "Tên gia tộc chỉ đồng ý các ký tự a-z,0-9 và chiều dài từ 5 đến 12 ký tự",//191
                "Tên gia tộc này đã được sử dụng, con hãy chọn tên khác.",//192
                "Hãy cố gắng phấn đấu làm 1 tộc trưởng thật tốt con nhé.",//193
                "%s đã đóng góp %d xu vào ngân sách gia tộc.",//194
                "Gia tộc đã được lên cấp %d",//195
                "Nhẫn thuật gia tộc cấp %d đã được khai mở.",//196
                "Gia tộc %s đã bị giải tán.",//197
                "Chiều dài tối đa là %d kí tự.",//198
                "Ghi chú của %s"
                        + "\n%s",//199
                "Khoảng cách quá xa không thể mời %s vào gia tộc.",//200
                "%s đang là thành viên gia tộc của bạn.",//201
                "%s đang là thành viên của gia tộc khác.",//202
                "Gia tộc đã đầy.",//203
                "Bạn đã gởi yêu lời  mời vào gia tộc. Xin vui lòng không mời liên tục.",//204
                "Lời mời vào gia tộc của bạn đã hết hạn.",//205
                "Khoảng cách quá xa không thể chấp nhận lời mời vào gia tộc.",//206
                "Bạn đang trong gia tộc này.",//207
                "Bạn đang trong gia tộc khác.",//208
                "%s đã rời khỏi gia tộc, bạn bị giảm %d xu",//209
                "Gia tộc bạn không đủ cấp độ để sử dụng vật phẩm này.",//210
                "%s đã gia nhập gia tộc %s.",//211
                "Bạn đang trong gia tộc này.",//212
                "Bạn đang trong gia tộc khác.",//213
                "Bạn đã gởi yêu cầu vào gia tộc rồi. Xin vui lòng không gởi liên tục.",//214
                "Khoảng cách quá xa không để gởi yêu cầu vào gia tộc.",//215
                "Yêu cầu vào gia tộc đã hết hạn.",//216
                "Khoảng cách quá xa không thể dồng ý %s vào gia tộc.",//217
                "%s đã bị đổi khỏi gia tộc, ngân sách gia tộc bị giảm %d xu",//218
                "Bạn bị đuổi khỏi gia tộc %s.",//219
                "%s đã rời khỏi gia tộc, %s giảm %d xu",//220
                "%s đã được bổ nhiệm làm tộc phó",//221
                "%s đã bị bãi nhiệm chức vụ làm thành viên",//222
                "%s đã được bổ nhiêm làm trưởng lão",//223
                "%d. Gia tộc %s trình độ cấp %d do %s làm tộc trưởng, thành viên %d/%d",//224
                "Bạn nhận được %d điểm cống hiến gia tộc",//225
                "Kinh nghiệm gia tộc không đủ",//226
                "Ngân sách gia tộc không đủ",//227
                "Gia tộc chưa đạt cấp độ yêu cầu",//228
                "Vật phẩm này chỉ bán cho tộc trưởng hoặc tộc phó.",//229
                "Gia tộc %s đã nhận được %s",//230
                "Chức năng đang bảo trì. Vui lòng chờ cho đến khi chức năng được cập nhật xong...",//260
                "Sau khi khai mở gia tộc mới được phép mua vật phẩm này.",//232
                "Cần có gia tộc để sử dụng vật phẩm này.",//233
                "Hệ thống",//234
                "Bạn nhận được %s",//235
                "Hôm này bạn bạn được %s",//236
                "Bạn nhận được %s yên",//237
                "Bạn nhận được %s xu",//238
                "Bạn nhận được %s lượng",//239
                "Bạn nhận được %s kinh nghiệm",//240
                "Hôm này bạn nhận được %s yên",//241
                "Hôm này bạn nhận được %s xu",//242
                "Hôm nay bạn nhận được %s lượng",//243
                "Hôm nay bạn nhận được %s kinh nghiệm",//244
                "Bạn không đủ xu để mua vật phẩm.",//245
                "Vật phẩm này đã hết hạn hoặc đã bị mua bởi người chơi khác",//246
                "Tối đa là %d nhỏ nhất là %d xu",//247
                "Bạn đã bán tối đa số lượng vật phẩm cho phép",//248
                "Chỉ có tộc trưởng hoặc tộc phó mới được phép mở cửa Lãnh Địa Gia Tộc.",//249
                "Số lần đi Lãnh Địa Gia Tộc trong tuần này đã hết.",//250
                "Không thể khảm cùng 1 loại ngọc lên 1 vật phẩm",//251
                "Không đủ điều kiện để khảm lên vật phẩm",//252
                "%s đã được gọt.",//253
                "Đã luyện ngọc lên cấp %d",//254
                "Nhập Kim Cương:",//255
                "Bạn chỉ có %s Kim Cương",//256
                "Đổi thành công bạn nhận được %s lượng.",//257
                "Đổi thành công bạn nhận được %s xu.",//258
                "Đổi thành công bạn nhận được %s yên.",//259
                "Bảng giá",//260
                "- Đổi %s Kim cương = %s lượng."
                        + "\n- Đổi %s Kim cương = %s xu."
                        + "\n- Đổi %s Kim cương = %s yên."
                        + "\n"
                        + "\nTỉ lệ:"
                        + "\n- Lượng X%s"
                        + "\n- Yên X%s"
                        + "\n- Xu X%s",//261
                "Bạn hiện đang có %s Kim Cương.",//262
                "%s đã mở cửa hang động.",//263
                "Trưởng nhóm phải là người báo danh thì ta mới chấp nhận mở cửa hang.",//264
                "Trình độ của con không thích hợp để vào cửa này.",//265
                "Số lần đi hang động của con trong ngày hôm nay đã hết hãy quay lại vào ngày hôm sau.",//266
                "%s cấp độ không phù hợp hoặc đã hết lượt vào trong hang.",//267
                "Hoạt động lần này con chỉ được phép đi một mình, không thể dẫn theo đồng đội.",//268
                "Hành trình khám phá hang động đã kết thúc, hãy đến Kanata đánh giá và nhận thưởng.",//269
                "%d. %s nhận được %d %s",//270
                "%s sử dụng rương hang động nhận được %s",//271
                "Cửa này vẫn chưa được mở.",//272
                "%s đã được mở.",//273
                "Cửa này chỉ chứa tối đa %d người.",//274
                "Chào mừng mọi người đã đến với server Nhẫn Giả, đang online %d. Chúc mọi người chơi game vui vẻ.",//275
                "Nạp Lượng Trên website nhé, Tỉ lệ :1000d = 1000 Lượng.",//276
                "Số lần vào hang động còn %d lần",//277
                "Điểm hiếu chiến của bạn hiện tại là %d",//278
                "Hộp ma quỷ",//279
                "Kẹo táo",//280
                "Chìa khóa",//281
                "Ma vật",//282
                "Không đủ MP để sử dụng",//283
                "Con không có đủ %d xu. Khi nào có đủ hãy quay lại đây gặp ta.",//284
                "Con không có đủ %d lượng. Khi nào có đủ hãy quay lại đây gặp ta.",//285
                "Không có chìa khóa.",//286
                "- Cách làm hộp ma quỷ: 5 xương thú + 2 tàn linh + 1 ma vật"
                        + "\n- Cách làm kẹo táo: Quả táo + 3 Mật ong + 20.000 xu."
                        + "\n- Cách đổi chìa khóa: 1 bộ HALLOWEEN + 10 lượng."
                        + "\n- Cách đổi đổi ma vật: 1 bộ HALLOWEEN.",//287
                "Con không thể thách đấu với chính bản thân.",//288
                "Hiện tại %s không có mặt ở đây.",//289
                "Ta đã gởi lời mời thách đấu đến %s",//290
                "Con đã có yêu cầu thách đấu từ trước. Vui lòng chờ cho đến khi yêu cầu thách đấu được kết thúc.",//291
                "%s đã có yêu cầu thách đấu với người khác.",//292
                "Nhập tên nhân vật",//293
                "%s thay đổi tiền đặt cược: %d xu",//294
                "Rời khỏi nơi này",//295
                "Đặt cược",//296
                "Nói chuyện",//297
                "Nhập xu cược",//298
                "Trận đấu bắt đầu.",//299
                "Không thể giao dịch ở trong khu vực này được.",//300
                "%s (%d) đang thách đấu với %s (%d) %d xu tại lôi đài.",//301
                "Phe %s đã giành chiến thắng nhận được %d xu.",//302
                "Phe %s và phe %s hòa nhau nhận được %d xu.",//303
                "Hiện tại phòng chiến đấu đã hết.",//304
                "Không thể đổi trạng thái PK ở trong khu vực này.",//305
                "Không thể tỷ thí ở trong khu vực này.",//306
                "Đặt cược tối thiểu là %d xu.",//307
                "Tài khoản này vẫn chưa được kích hoạt vui soạn tin ON KHKMT %s gửi 8585 (5000đ) để kích hoạt.",//308
                "Không có đủ. %s.",//309
                "Phân thân không thể sử dụng chức năng này.",//310
                "%s đã lên cấp độ %d.",//311
                "Phân thân khổng thể sử dụng vật phẩm này.",//312
                "Tạm thời bảo trì trong vài ngày tới nha ^_^",//313
                "Vật phẩm này chỉ dùng cho cấp độ từ %d đến %d.",//314
                "Sinh lực đã đầy.",//315
                "Nhiên liệu đã đầy.",//316
                "Bảo trì đến năm sau ^_^ ...>>>>.",//317
                "Cần phải hội tụ 7 viên ngọc thì mới có tác dụng.",//318
                "Thuộc tính không hợp lệ.",//319
                "Khu vực quá tải.",//320
                "Bạn đã triệu hồi Boss tuần lộc.",//321
                "Bánh khúc Chocolate",//322
                "Bánh khúc Dâu tây",//323
                "- Cách làm Bánh khúc Chocolate: 5 Bơ + 5 Kem + 5 Đường bột + 1 Chocolate."
                        + "\n- Cách làm Bánh khúc Dâu tây: 5 Bơ + 5 Kem + 5 Đường bột + 2 Dâu tây"
                        + "\n- Cách làm hộp quà trang trí : 3 Trái châu + 3 Dây kim tuyến + 3 Chuông vàng + 15 lượng",//324
                "Tối đa số lượng con có thể nhập vào là %d.",//325
                "Đang thả câu...",//326
                "Chỉ có thể thả câu tại nơi có nước.",//327
                "Chỉ bán sau %s",//328
                "Con không có đồng xu để đổi.",//329
                "Mỗi ngày chiến trường chỉ mở 3 lần: 13-14h30', 16-17h30', 19h-20h30'",//330
                "Bạn không thể vào được căn cứ địa.",//331
                "Bạn vừa đánh trọng thương %s",//332
                "Bạn bị %s đánh trọng thương",//333
                "Không thể dùng ngọc cấp cao hơn để luyện",//334
                "Tổng kết",//335
                "%sBạch Giả: %d điểm"
                        + "\nHắc Giả: %d điểm"
                        + "\n%s",//336
                "%d. %s: %d điểm (%s)"
                        + "\nDanh hiệu: %s",//337
                "Hắc",//338
                "Bạch",//339
                "Lỗi",//340
                "Hắc giả",//341
                "Bạch giả",//342
                "%s giành chiến thắng"
                        + "\nc0",//343
                "Chiến trường đã kết thúc",//344
                "Hắc giả và bạch giả hòa nhau"
                        + "\nc0",//345
                "Tuần thú lệnh không cho phép dùng ở nơi này.",//346
                "Long đầu trụ sẽ suất hiện từ khu 1 - khu 10 chỉ sau vài phút nữa.",//347
                "%s đã bị tiêu diệt.",//348
                "Cửa báo danh chiến trường sẽ mở sau 1 phút nữa. Hãy đến gặp NPC Rikudo để báo danh.",//349
                "Cửa báo danh chiến trường đã kết thúc.",//350
                "Con đã báo danh bên phe hắc giả không thể báo danh bên này nữa.",//351
                "Con đã báo danh bên phe bạch giả không thể báo danh bên này nữa.",//352
                "Trình độ từ cấp 50 trở lên mới được tham gia.",//353
                "Không đủ %d điểm %s",//354
                "Nhiệm vụ",//355
                "%s"
                        + "\n- Sử dụng trang bị %s."
                        + "\n%s",//356
                "Con đã hoàn thành nhiệm vụ cho ngày hôm nay",//357
                "Con chưa nhậm nhiệm vụ.",//358
                "Nhiệm vụ của con đã được hủy.",//359
                "Con hãy hoàn thành nhiệm vụ đã được giao trước",//360
                "Con không có nhiệm vụ để trả",//361
                "Bạn có muốn hủy nhiệm vụ %s không?",//362
                "Thông báo",//363
                "Đã hủy nhiệm vụ %s",//364
                "%s- Có thể nhận thêm %d nhiệm vụ trong ngày."
                        + "\n- Sử dụng Danh vọng phù để tăng số lần nhận nhiệm vụ trong ngày."
                        + "\n- Có thể sử dụng thêm %d Danh vọng phù trong ngày.",//365
                "Hoàn thành nhiệm vụ, hãy gặp Ameji để trả nhiệm vụ",//366
                "Hiện tại bạn có thể nhận thêm %d nhiệm vụ",//367
                "Hãy sử dụng Nguyệt Nhãn để sử dụng được chức năng này.",//368
                "Nguyệt Nhãn đã đạt cấp tối đa.",//369
                "Bạn có muốn nâng cấp %s với %d yên hoặc xu và %d lượng với tỷ lệ thành công là %d%s không?",//370
                "Con phải có %d ô trống trong hành trang",//371
                "Con không có đủ điều kiện để nâng cấp.",//372
                "Nâng cấp thất bại",//373
                "Nâng cấp thành công",//374
                "Không đủ %d đá danh vọng cấp %s",//375
                "Bạn có muốn nâng cấp %s với %d yên hoặc xu với tỷ lệ thành công là %d%s không?",//376
                "Nhận được",//377
                "- %d điểm danh vọng %s%s",//378
                "- Sử dụng trang bị %s. %s",//379
                "Hộp quà Trang trí",//380
                "Trình độ của con phải đạt cấp %d mới được tham gia.",//381
                "Hành trang con không có đủ %s để tham gia.",//382
                "Yêu cầu nhập học để sử dụng chức năng này.",//383
                "Con phải có đủ %d lượng mới có thể tu luyện bí kíp.",//384
                "Để luyện bí kíp cần:"
                        + "\n- Lượng: 1000."
                        + "\n- Chuyển tinh thạch: 1."
                        + "\n- Đá cấp 12: 1.",//385
                "Hành trang con không có đủ nguyên liệu. Hãy xem hướng dẫn nguyên liệu.",//386
                "Môn phái không thích hợp.",//387
                "Để nâng cấp %s cần %d lượng và %d %s với tỷ lệ thành công là %d%s. Con có đồng ý không.",//388
                "Để nâng cấp con hãy sử dụng bí kíp.",//389
                "Bí kíp đã đạt cấp tối đa.",//390
                "Cần có đủ %d %s để tu luyện %s.",//391
                "Tu luyện %s thành công.",//392
                "Tu luyện %s thất bại.",//393
                "Bánh chưng",//394
                "Bánh tét",//395
                "-Bánh chưng: 5 lá dong + 5 nếp + 3 thịt heo + 5 đậu xanh + 5 dây buộc."
                        + "\n-Bánh tét: 2 lá dong + 2 nếp + 2 đậu xanh + 2 dây buộc, 50k xu",//396
                "%d. %s đang có %s điểm",//397
                "Chúc mừng bạn đã tạo được %s",// 398
                "Bạn không đủ mảnh %s để nâng cấp.", //399
                "%s đã được nâng cấp tối đa.", //400
                "Chúc mừng bạn đã nâng cấp %s lên cấp %s.",// 401
                "%s đã được cắm chìa khoá vào cơ quan %s", // 402
                "Bạn nhận được %d đồng tiền gia tộc", // 403
                "Đã có người vào trong ải, hoặc chưa tới lượt đánh của bạn. Vui lòng chờ ở bên ngoài.", //404
                "%s đã rời ải, xin mời thành viên tiếp theo.",   // 405
                "%s đã nhận được %s rơi ra từ %s", // 406
                "Thất thú ải sẽ mở cửa vào lúc 9h30' đến 10h30' và từ 21h30' đến 22h30'", // 407
                "Thất thú ải chỉ mở cửa 2 lần vào lúc 9h đến 10h30' và từ 21h đến 22h30' vào thứ 3,5,7 hàng tuần.", //408
                "Bạn cần phải tháo trang bị vĩ thú đang sử dụng",//409
                "Bạn cần có vĩ thú để sử dụng",//410
                "Cần phải tháo hết trang bị vĩ thú ra trước",//411
                "Thất thú ải đã mở cửa báo danh, hay mau chóng tập hợp đồng đội để báo danh tham gia.",// 412
        };
    }
}
