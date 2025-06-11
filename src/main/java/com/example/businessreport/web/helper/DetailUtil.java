package com.example.businessreport.web.helper;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public final class DetailUtil {

  // インスタンス生成禁止
  private DetailUtil() {
  }

  // 各ViewModeに対応するレコードクラス
  public record DetailShow(boolean showList, boolean showCreate, boolean showBrowse, boolean showUpdate) {
  }

  // 表示モードを表すEnumを定義
  // 各Enum定数が対応するDetailShowインスタンスを持つ
  public enum ViewMode {
    LIST(true, false, false, false),
    CREATE(false, true, false, false),
    BROWSE(false, false, true, false),
    UPDATE(false, false, false, true);

    // 各ViewModeに対応するDetailShowインスタンス
    private final DetailShow detailShow;

    // Enumコンストラクタ
    ViewMode(boolean showList, boolean showCreate, boolean showBrowse, boolean showUpdate) {
      this.detailShow = new DetailShow(showList, showCreate, showBrowse, showUpdate);
    }

    // 外部からDetailShowインスタンスを取得するメソッド
    public DetailShow getDetailShow() {
      return this.detailShow;
    }
  }

  // EnumMapを使用して、EnumとDetailShowの関連付けを行う（オプション）
  // EnumMapはEnumをキーとするMapで、HashMapよりも効率的
  private static final Map<ViewMode, DetailShow> SHOW_MAP;
  static {
    EnumMap<ViewMode, DetailShow> map = new EnumMap<>(ViewMode.class);
    for (ViewMode mode : ViewMode.values()) {
      map.put(mode, mode.getDetailShow());
    }
    SHOW_MAP = Collections.unmodifiableMap(map); // 変更不可にする
  }

  // ViewMode Enumを受け取るようにする
  public static DetailShow getDetailShow(ViewMode mode) {
    return SHOW_MAP.get(mode);
  }

}
