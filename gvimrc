"=============================================================================
"    Description: .gvimrcサンプル設定
"         Author:
"  Last Modified: 0000-00-00 00:00
"        Version: 0.00
"=============================================================================
scriptencoding utf-8
"----------------------------------------
" システム設定
"----------------------------------------
" エラー時の音とビジュアルベルの抑制
set noerrorbells
set novisualbell
set visualbell t_vb=

if has('multi_byte_ime') || has('xim')
  set iminsert=0 imsearch=0
  if has('xim') && has('GUI_GTK')
    " XIMの入力開始キー
    " set imactivatekey=C-space
  endif
endif

" IMEの状態をカラー表示
if has('multi_byte_ime')
  highlight Cursor guifg=NONE guibg=Green
  highlight CursorIM guifg=NONE guibg=Purple
endif

"----------------------------------------
" 表示設定
"----------------------------------------
" ツールバーを非表示
" set guioptions-=T
" コマンドラインの高さ
" set cmdheight=2

" カラー設定:
colorscheme jellybeans
"colorscheme pablo

" フォント設定
" フォントは英語名で指定すると問題が起きにくくなります
"if has('xfontset')
"  " set guifontset=a14,r14,k14
"elseif has('unix')
"
"elseif has('mac')
"  " set guifont=Osaka-Mono:h14
"elseif has('win32') || has('win64')
"  " set guifont=MS_Gothic:h12:cSHIFTJIS
"  " set guifontwide=MS_Gothic:h12:cSHIFTJIS
"endif

" フォント設定
set guifont=Ricty:h12:cSHIFTJIS
set transparency=250

" 印刷用フォント
if has('printer')
  if has('win32') || has('win64')
  " set printfont=MS_Mincho:h12:cSHIFTJIS
  " set printfont=MS_Gothic:h12:cSHIFTJIS
  endif
endif

""""""""""""""""""""""""""""""
" Window位置の保存と復帰
""""""""""""""""""""""""""""""
if has('unix')
  let s:infofile = '~/.vim/.vimpos'
else
  let s:infofile = '~/_vimpos'
endif

function! s:SaveWindowParam(filename)
  redir => pos
  exec 'winpos'
  redir END
  let pos = matchstr(pos, 'X[-0-9 ]\+,\s*Y[-0-9 ]\+$')
  let file = expand(a:filename)
  let str = []
  let cmd = 'winpos '.substitute(pos, '[^-0-9 ]', '', 'g')
  cal add(str, cmd)
  let l = &lines
  let c = &columns
  cal add(str, 'set lines='. l.' columns='. c)
  silent! let ostr = readfile(file)
  if str != ostr
    call writefile(str, file)
  endif
endfunction
augroup SaveWindowParam
  autocmd!
  execute 'autocmd SaveWindowParam VimLeave * call s:SaveWindowParam("'.s:infofile.'")'
augroup END

if filereadable(expand(s:infofile))
  execute 'source '.s:infofile
endif
unlet s:infofile

"----------------------------------------
"メニューアイテム作成
"----------------------------------------
silent! aunmenu &File.Save
silent! aunmenu &File.保存(&S)
silent! aunmenu &File.差分表示(&D)\.\.\.

let message_revert="再読込しますか?"
amenu <silent> 10.330 &File.再読込(&U)<Tab>:e!  :if confirm(message_revert, "&Yes\n&No")==1<Bar> e! <Bar> endif<CR>
amenu <silent> 10.331 &File.バッファ削除(&K)<Tab>:bd  :confirm bd<CR>
amenu <silent> 10.340 &File.保存(&W)<Tab>:w  :if expand('%') == ''<Bar>browse confirm w<Bar>else<Bar>confirm w<Bar>endif<CR>
amenu <silent> 10.341 &File.更新時保存(&S)<Tab>:update  :if expand('%') == ''<Bar>browse confirm w<Bar>else<Bar>confirm update<Bar>endif<CR>
amenu <silent> 10.400 &File.現バッファ差分表示(&D)<Tab>:DiffOrig  :DiffOrig<CR>
amenu <silent> 10.401 &File.裏バッファと差分表示(&D)<Tab>:Diff\ #  :Diff #<CR>
amenu <silent> 10.402 &File.差分表示(&D)<Tab>:Diff  :browse vertical diffsplit<CR>

"----------------------------------------
"個人設定
"----------------------------------------
"
" Ctrl+Vの挙動を変更
nmap <C-v> <C-v>
cmap <C-v> <C-v>

"入力モード時、ステータスラインのカラーを変更
augroup InsertHook
autocmd!
autocmd InsertEnter * highlight StatusLine guifg=NONE  guibg=Red
autocmd InsertLeave * highlight StatusLine guifg=NONE guibg=DarkCyan
augroup END

"全角スペースを視覚化
highlight ZenkakuSpace cterm=underline ctermfg=lightblue guibg=#666666
au BufNewFile,BufRead * match ZenkakuSpace /　/

"ウィンドウを最大化して起動
au GUIEnter * simalt ~x

"検索関連
set ignorecase          " 大文字小文字を区別しない
set smartcase           " 検索文字に大文字がある場合は大文字小文字を区別
set incsearch           " インクリメンタルサーチ
set hlsearch            " 検索マッチテキストをハイライト

" バックスラッシュやクエスチョンを状況に合わせ自動的にエスケープ
cnoremap <expr> / getcmdtype() == '/' ? '\/' : '/'
cnoremap <expr> ? getcmdtype() == '?' ? '\?' : '?'

"編集関連
set showmatch           " 対応する括弧などをハイライト表示する
set matchtime=3         " 対応括弧のハイライト表示を3秒にする
" 対応括弧に'<'と'>'のペアを追加
set matchpairs& matchpairs+=<:>

" バックスペースでなんでも消せるようにする
set backspace=indent,eol,start




"カーソル行の協調表示
set cursorline

"ノーマルモード中のエンター改行
noremap <CR> O<ESC>
noremap <S-CR> o<ESC>

" カレントディレクトリを自動的に移動
augroup BufferAu
    autocmd!
    " カレントディレクトリを自動的に移動
    autocmd BufNewFile,BufRead,BufEnter * if isdirectory(expand("%:p:h")) && bufname("%") !~ "NERD_tree" | cd %:p:h | endif
augroup END

"ビジュアルモード時vで行末まで選択
vnoremap v $

"行の折り返しキーマップ
nnoremap <Leader>w  :set wrap!<CR>

" .vimrcを開く
nnoremap <Space>.  :<C-u>edit $MYGVIMRC<CR>
" source ~/.vimrc を実行する。
nnoremap <Space>,  :<C-u>source $MYGVIMRC<CR>

" 移動コマンドを使ったとき、行頭に移動しない
"set startofline

" カーソルを行頭、行末で止まらないようにする
set whichwrap=b,s,h,l,<,>,[,]


"カーソル位置の単語とヤンクした文字列を置換する
nnoremap <silent> ciy ciw<C-r>0<ESC>:let@/=@1<CR>:noh<CR>
nnoremap <silent> cy   ce<C-r>0<ESC>:let@/=@1<CR>:noh<CR>
vnoremap <silent> cy   c<C-r>0<ESC>:let@/=@1<CR>:noh<CR>

"タブのスペース数
set tabstop=4

"シフト幅の大きさ
set shiftwidth=4

"初期設定折り返し無
set wrap!

"全選択
nnoremap <C-a> ggVG
inoremap <C-a> <ESC>ggVG

"カーソル行以下全選択
nnoremap <C-z> VG

"カーソル行以上全選択
nnoremap <C-q> Vgg

" +, -
nnoremap + <C-a>
nnoremap - <C-x>

"現在日時挿入
inoremap <C-t>  <C-r>=strftime('%Y/%m/%d/%H:%M')<Return>

"置換補助コマンド
nnoremap <C-h> :%s///g

"ヴィジュアルモードで選択した文字列を検索
vnoremap <silent> * "vy/\V<C-r>=substitute(escape(@v,'\/'),"\n",'\\n','g')<CR><CR>

"置換補助コマンド（ヴィジュアルモードで選択した文字列を置換対象にする）
vnoremap <C-e> "wy :%s/\V<C-r>=substitute(escape(@w,'/'),"\n",'\\n','g')<CR>//g

"ダブルクリックで単語検索
nnoremap <2-LeftMouse> g*

"挿入文字を含まない行を削除（gは含む）
nnoremap <C-g> :v//d

"新規タブ追加
nnoremap <C-t> :tabe<CR>

"distinct処理
vnoremap <C-d> :!sort<CR>:g/^\(.*\)\n^\1$/d<CR>

"行のカンマ連結（ヴィジュアルモード）
vnoremap ,J J:s/ /,/g<CR>
"
"カンマ連結文字列を縦に並べる（ヴィジュアルモード）
vnoremap ,t :s/,/\r/g<CR>

"空行削除
nnoremap <C-n> :g/^$/d<CR>

"ダウンコピー
"function! s:downCopy() range
"    echo a:firstline
"    echo virtcol('.')
"	echo a:lastline
"endfun
"vnoremap <C-m>  :call <SID>downCopy()<CR>

"括弧補助コマンド
inoremap { {}<LEFT>
inoremap [ []<LEFT>
inoremap ( ()<LEFT>
inoremap " ""<LEFT>
inoremap ' ''<LEFT>
inoremap < <><LEFT>
inoremap （ （）<LEFT>
inoremap 【 【】<LEFT>
inoremap 「 「」<LEFT>

vnoremap <Up>	 k
vnoremap <Down>  j	
vnoremap <Left>	 h
vnoremap <Right> l	

inoremap <C-l> <RIGHT>
inoremap <C-h> <LEFT>
inoremap <C-k> <UP>
inoremap <C-j> <DOWN>
inoremap <C-e> <END>
inoremap <C-f> <ESC>^i
inoremap <C-d> <ESC>ddi

inoremap <C-b> ⇒

" 検索後にジャンプした際に検索単語を画面中央に持ってくる
nnoremap n nzz
nnoremap N Nzz
nnoremap * *zz
nnoremap # #zz
nnoremap g* g*zz
nnoremap g# g#zz

" j, k による移動を折り返されたテキストでも自然に振る舞うように変更
nnoremap j gj
nnoremap k gk

"vnoremap <Up>	 l
"vnoremap <Down>  k	
"vnoremap <Left>	 ;
"vnoremap <Right> j	
"
"inoremap <C-l> <UP>
"inoremap <C-k> <DOWN>
"inoremap <C-;> <LEFT>
"inoremap <C-j> <RIGHT>
"
" Ctrl + hjkl でウィンドウ間を移動
nnoremap <C-h> <C-w>h
nnoremap <C-j> <C-w>j
nnoremap <C-k> <C-w>k
nnoremap <C-l> <C-w>l

" Shift + 矢印でウィンドウサイズを変更
nnoremap <S-Left>  <C-w><<CR>
nnoremap <S-Right> <C-w>><CR>
nnoremap <S-Up>    <C-w>-<CR>
nnoremap <S-Down>  <C-w>+<CR>

"行の中央にジャンプ"
function! s:JumpMiddle()
let end = col('$')-1
let middle = float2nr(ceil(end/2))
let save_cursor = getpos(".")
let save_cursor[2] = middle
call setpos('.', save_cursor)
endfun
nnoremap <silent> M :call <SID>JumpMiddle()<CR>

"現在のカーソルと行末の中央にジャンプ"
function! s:JumpMaeMiddle()
let now_cursor = getpos(".")
let dist = float2nr(ceil(now_cursor[2]/2)) 
let now_cursor[2] = dist
call setpos('.', now_cursor)
endfun
nnoremap <silent> m :call <SID>JumpMaeMiddle()<CR>

"現在のカーソルと行末の中央にジャンプ"
function! s:JumpAtoMiddle()
let end = col('$')-1
let now_cursor = getpos(".")
let middle = (end - now_cursor[2])
let dist = float2nr(ceil(middle/2)) 
let now_cursor[2] = dist + now_cursor[2]
call setpos('.', now_cursor)
endfun
nnoremap <silent> ; :call <SID>JumpAtoMiddle()<CR>

"ファイラーの起動"
nnoremap <silent><Space>f    :Explore<CR>

"swapファイルを作成しない
set noswapfile

"バックアップファイルを作成しない
set nobackup
set nowritebackup

"他のアプリでコピーした文字をVimで貼付ける"
set clipboard=unnamed,autoselect

"ESCコマンド"
inoremap <C-c> <Esc>
inoremap jj <Esc>

"コピーした文字で繰り返し上書きペースト可能にする"
vnoremap <silent> <C-p> "0p<CR>

"fileencording 
"set fenc=utf-8

" デフォルトエンコーディング
set encoding=utf-8

"" デフォルト改行コード(LF)
"set fileformat=unix

" Indent
nnoremap > >>
nnoremap < <<

" <TAB>: indent.
xnoremap <TAB>  >
" <S-TAB>: unindent.
xnoremap <S-TAB>  <

"数値の連番挿入
"nnoremap <C-n> yyp<C-a>

"nnoremap gc `[v`]
"vnoremap gc :<C-u>normal gc<Enter>
"onoremap gc :<C-u>normal gc<Enter>

"透明化
set transparency=230
"
" Folding {{{

set foldmethod=marker

" Toggle folding

nnoremap \ za
vnoremap \ za

" }}}


"********************************************************************************
"*plugin
"********************************************************************************


"------------------------------------
" unite.vim
"------------------------------------
" 入力モードで開始する
"let g:unite_enable_start_insert=1
"" バッファ一覧
"noremap <C-U><C-B> :Unite buffer<CR>
"
"" ファイル一覧
""noremap <C-U><C-F> :UniteWithBufferDir -buffer-name=files file<CR>
""vimfilerを開くIDE風
"nnoremap <C-U><C-F> :VimFilerBufferDir -split -simple -winwidth=35 -no-quit<CR>
"
"" 最近使ったファイルの一覧
"noremap <C-U><C-R> :Unite file_mru<CR>
""最近開いたファイル履歴の保存数
"let g:unite_source_file_mru_limit = 100
"
""vimfilerがspace+l,space+j,space+mに割り当てられる
"let g:vimfiler_as_default_explorer = 1
"let g:vimfiler_edit_action = 'tabopen'
"
""セーフモードを無効にした状態で起動する
"let g:vimfiler_safe_mode_by_default = 0
"
""レジスタ一覧
"noremap <C-U><C-Y> :Unite -buffer-name=register register<CR>
"" ファイルとバッファ
"noremap <C-U><C-U> :Unite buffer file_mru<CR>
"" 全部
"noremap <C-U><C-A> :Unite UniteWithBufferDir -buffer-name=files buffer file_mru bookmark file<CR>
"" ESCキーを2回押すと終了する
""au FileType unite nnoremap <silent> <buffer> <ESC><ESC> :q<CR>
""au FileType unite inoremap <silent> <buffer> <ESC><ESC> <ESC>:q<CR>
"
""file_mruの表示フォーマットを指定。空にすると表示スピードが高速化される
"let g:unite_source_file_mru_filename_format = ''
"
""uniteを開いている間のキーマッピング
"autocmd FileType unite call s:unite_my_settings()
"function! s:unite_my_settings()"{{{
"  "ESCでuniteを終了
"  nmap <buffer> <ESC> <Plug>(unite_exit)
"  "入力モードのときjjでノーマルモードに移動
"  imap <buffer> jj <Plug>(unite_insert_leave)
"  "入力モードのときctrl+wでバックスラッシュも削除
"  imap <buffer> <C-w> <Plug>(unite_delete_backward_path)
"  "ctrl+jで縦に分割して開く
"  nnoremap <silent> <buffer> <expr> <C-j> unite#do_action('split')
"  inoremap <silent> <buffer> <expr> <C-j> unite#do_action('split')
"  "ctrl+lで横に分割して開く
"  nnoremap <silent> <buffer> <expr> <C-l> unite#do_action('vsplit')
"  inoremap <silent> <buffer> <expr> <C-l> unite#do_action('vsplit')
"  "ctrl+oでその場所に開く
"  nnoremap <silent> <buffer> <expr> <C-o> unite#do_action('open')
"  inoremap <silent> <buffer> <expr> <C-o> unite#do_action('open')
"endfunction"}}}
"
"set nocompatible               " be iMproved
"filetype off




