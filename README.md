# ToDoアプリ 「ToDo!!」
![todo-sda-ex_main](https://user-images.githubusercontent.com/66711292/126701147-faeb1dac-d640-45e7-8c2a-ff7c2244f18d.png)<br><br>
作成したToDoをリストに登録していく形式で、管理を行うことができるToDoアプリです。<br><br>

ユーザーはToDoに設定した期限や優先度を基にリストのソートを行い、達成すべきToDoから取り掛かることができます。<br>
完了したり期限切れになったToDoは個別のリストで管理されるため、達成・未達成のログのような使い方ができるほか、
内容を修正して再度ToDo一覧に登録し直すこともできます。<br><br>

レスポンシブ対応を行っているため、PCのほかスマートフォン等でもご利用いただけます。
<br><br>

## 作成した目的・意識した点
Spring FrameWorkを中心に、Javaでアプリケーション開発を行うための技術やツール、知識を学習してきた成果として、
何らかのアプリを作りたいと考えました。<br>
ToDoアプリサイトは以前にもJava Servletを用いて作成したことがあるため、習得した技術や知識に対する理解を深めつつ、
それらの違いや有用性を知ることができると思い、作成することを決めました。<br><br>

シンプルな題材になる分、得るものが少なくならないように、特に以下の2点を意識して作成を進めました。
### 開発における実践的な技術や手法を多く取り入れる ###
Java開発のスタンダードなフレームワークであるSpring Frameworkの機能をしっかりと活用することをはじめ、
実際の開発現場で使われている技術をなるべく取り入れるように意識しました。<br>
* テストの作成
* 静的解析ツールを活用したリファクタリング
* GitHubの機能(Issue、Pull request等)を活用した開発手順
* CI/CDツールによる自動テスト、自動デプロイ &emsp;&emsp;等<br><br>

中には理解が足りず、導入当初は正しく活用できていなかったものもありましたが、実践のなかで「この方法では意味がない。こうすれば便利になる」と考えて改善していくことで、開発をする視点での有益さを理解することができました。<br><br>

具体的に使用した技術やツールについては、[使用技術・ツール](#used)をご覧いただければ幸いです。
<br><br>

### 使いやすい・見やすいと感じるアプリにする ###
単純に必要な機能があるだけではなく、しっかりと活用できるToDoアプリにすることを意識しました。<br><br>

ToDoアプリにおいては、基本的な 登録 -> 確認 -> 完了 の流れがスムーズに行えることが、使いやすさにおいて最重要であると考え、
ここを軸に一覧のレイアウトや各ボタンの配置等を工夫しました。<br>
また、このようなアプリはモバイル端末から利用するからこそ便利なものであると考え、そちらでも快適性を崩さないためのレスポンシブデザインについては、特に力を入れて行いました。<br><br>

サイトのデザインやUIについては、随時、知人に見せてアドバイスをもらい、それを基に修正を行いました。<br>
その結果、自分とは異なる視点から改善点を探すことができ、また、他者の意見を自分の製作物に反映する経験を得られたと感じています。
<br><br>

## サイトURL
https://todo-sda-ex.herokuapp.com/<br><br>
**※サイト内のお試しログインボタンから、テストユーザーで機能をお試しいただけます。**
<br><br>

## 開発環境
* Java 11.0.2
* Spring Boot 2.3.4
* HTML / CSS
* JavaScript
* JQuery 3.6.0
* Spring Tool Suite 4.7.0
<br><br>

<a id = "used"></a>
## 仕様技術・ツール
* Heroku (プラットフォーム)
* Git / GitHub (バージョン管理)
* Gradle (ビルドツール)
* CircleCI (自動テスト、自動デプロイ）
* 各Springフレームワーク
  * Spring MVC (フロントコントローラパターン)
  * Spring Data JPA (データベースアクセス、ORM)
  * Spring Security (アクセス認証、認可)
  * Spring Test (テスト)
* PostgreSQL (データベース)
* H2 Database (テスト用組み込みデータベース)
* JUnit5 (テスト)
* Thymeleaf (テンプレートエンジン)
* Bootstrap4 (CSSフレームワーク)
* Lombok (ボイラープレートの自動生成)
* 静的解析ツール
  * Checkstyle (コードチェック)
  * SpotBugs (バグチェック)
  * Jacoco (テストカバレッジ)
<br><br>

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
登録されているToDoの詳細を確認し、内容の変更やToDoの削除を行うことができます。<br>
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
