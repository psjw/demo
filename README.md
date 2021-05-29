https://spring.io/guides/gs/serving-web-content/
참고 하여 생성

marko
https://markojs.com/docs/installing/


C:\ljw-study\codesoom\demo1>npx @marko/create
npx: installed 141 in 12.632s
√ Type your project name · web
√ Choose a template · Default starter app
√ Project created! To get started, run:

    cd web
    npm run dev


C:\ljw-study\codesoom\demo1>cd web

C:\ljw-study\codesoom\demo1\web>npm run dev

> web@1.0.0 dev C:\ljw-study\codesoom\demo1\web
> marko-serve ./src/pages

You can now view src\pages in your browser

Local Address:   http://localhost:3000
On Your Network: http://192.168.56.1:3000

Note that marko serve is only intended for development
You can create a production-ready build using marko build


Compiled in 10.7s

Compiling...


C:\ljw-study\codesoom\demo1\web>npm install axios
npm WARN optional SKIPPING OPTIONAL DEPENDENCY: fsevents@1.2.13 (node_modules\fsevents)
:_locks
npm WARN notsup SKIPPING OPTIONAL DEPENDENCY: Unsupported platform for fsevents@1.2.13:
wanted {"os":"darwin","arch":"any"} (current: {"os":"win32","arch":"x64"})

+ axios@0.21.1
  added 1 package from 1 contributor and audited 1358 packages in 8.698s

93 packages are looking for funding
run `npm fund` for details

found 2 low severity vulnerabilities
run `npm audit fix` to fix them, or `npm audit` for details

