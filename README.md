# ToDoアプリ 「ToDo!!」
![todo-sda-ex_main](https://user-images.githubusercontent.com/66711292/126701147-faeb1dac-d640-45e7-8c2a-ff7c2244f18d.png)<br><br>
作成したToDoをリストに登録していく形式で、管理を行うことができるToDoアプリです。<br><br>

ユーザーはToDoに設定した期限や優先度を基にリストのソートを行い、達成すべきToDoから取り掛かることができます。<br>
完了したり期限切れになったToDoは個別のリストで管理されるため、達成・未達成のログのような使い方ができるほか、
内容を修正して再度達成すべきToDoとして登録し直すこともできます。<br><br>

**サイト内のお試しログインボタンから、テストユーザーで機能をお試しいただけます。**<br><br>

レスポンシブ対応を行っているため、PCのほかスマートフォン等でもご利用いただけます。
<br><br>

## 作成の経緯・目的

## サイトURL
https://todo-sda-ex.herokuapp.com/

## 開発環境

## 使用技術

## 各画面説明
### トップページ(ホーム)
![todo-sda-ex_top](https://user-images.githubusercontent.com/66711292/126701202-827cdeb0-ea3b-4f25-a702-ddf23de0d5d7.png)<br><br>
ユーザーが最初にアクセスするページです。<br>
サイトの説明やよくある質問などが記載されています。
<br><br><br>

***
### ログイン画面
![todo-sda-ex_login](https://user-images.githubusercontent.com/66711292/126701260-bd7152b8-a533-4d50-b7e7-f48d6aca2c75.png)<br><br>
ユーザー登録を行ったIDとパスワードでログインを行います。<br>
お試しログインボタンから、ワンボタンでテストユーザーログインを行うことができます。<br>
ログインユーザーの認証情報はSpring Securityによって管理しています。
<br><br><br>

***
### ToDo一覧画面(メイン画面)
![todo-sda-ex_todoList](https://user-images.githubusercontent.com/66711292/126712650-f4123112-1cc8-464b-bc60-1a8d939bd734.png)<br><br>
登録されたToDoがリストで表示されます。<br>
ToDoのタイトルを選択すると、ToDoの詳細を確認することができます。<br>
リストは3つのタブで表示を切り替えることができます。
* 一覧タブ : ユーザーが達成すべきToDoが表示される
* 完了タブ : 完了済みのToDoが表示される
* 期限切れタブ : 設定期限を過ぎているToDoが表示される<br>

また、期限が当日中のToDoが存在する場合はリスト上方に警告が表示され、選択すると対象のToDoを確認できます。
<br><br>
並び順ドロップダウンからは期限、優先度、更新日時でリストのソートを行うことができます。<br>
リスト右端のアイコンを選択することで、ToDoを完了させる(完了タブの場合は未完了に戻す)ことができます。
<br><br><br>

***
### ToDo追加画面
![todo-sda-ex_addTodo](https://user-images.githubusercontent.com/66711292/126701286-4021fead-83a3-4b96-a2af-a68273714be6.png)<br><br>
タイトル、期限、優先度、メモ（任意）を設定して、ToDoの追加を行うことができます。
<br><br><br>

***
### ToDo詳細画面
![todo-sda-ex_todoDetails](https://user-images.githubusercontent.com/66711292/126701298-d1593de5-2158-4316-833e-d58d311b0069.png)<br><br>
登録されているToDoの詳細を確認し、内容の変更を行うことができます。<br>
<br><br><br>

***
### お問い合わせ画面
![todo-sda-ex_inquiry](https://user-images.githubusercontent.com/66711292/126712937-21e2be0f-cbcb-4f95-bd02-ac7f57360bcb.png)<br><br>
ご意見・ご要望や不具合の報告のためのお問い合わせを行うことができます。<br>
入力された内容は、Spring Mailによってお問い合わせ用Gmailアドレスに宛てて送信されます。
<br><br><br>

## スマートフォン操作の様子
Androidスマートフォンでアプリを利用する様子です。<br>
* 一覧画面でToDoのリストを確認
* 新しいToDoを追加
* リストを優先度でソート
* 追加したToDoを完了させる<br>

の手順を行っています。<br><br>

![todo-sda-ex_demoVideo](https://user-images.githubusercontent.com/66711292/126734629-01c23fc9-24b7-4061-9890-d467e8a92cad.gif)
