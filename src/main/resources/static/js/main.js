// 新規作成ボタン要素
const openBtn = document.querySelector('.open-add-report-btn');

// 新規作成画面の閉じるボタン要素
const closeBtn = document.querySelector('.modal-dialog-close-btn');
// 新規作成画面要素
const modalDialog = document.querySelector('.modal-dialog');

// 新規作成ボタンクリック時
openBtn?.addEventListener('click', async () => {
  // console.log('test');
  // モーダルを開く
  modalDialog.showModal();
  // モーダルダイアログを表示する際に背景部分がスクロールしないようにする
  document.documentElement.style.overflow = "hidden";
});

// モーダルの閉じるボタンをクリック時
closeBtn?.addEventListener('click', () => {
  // モーダルを閉じる
  modalDialog.close();
  // モーダルを解除すると、スクロール可能になる
  document.documentElement.removeAttribute("style");
});

// 新規作成ボタンクリック時
openBtn?.addEventListener('mousedown', async () => {
  // console.log('test');
  // // モーダルを開く
  // modalDialog.showModal();
  // // モーダルダイアログを表示する際に背景部分がスクロールしないようにする
  // document.documentElement.style.overflow = "hidden";
});
