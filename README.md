<h1>2 - Banco Digital - Androidstudio - Kotlin</h1>
<p>Nesse aplicativo mais robusto tem a utilização do Firebase, implementação da arquitetura MVVM ( facilitando implementações e manultenções futuras ) e um layout agradavel para o usuario fazer transações de simples manuseio ( fictício )</p>

<h2>Funcionalidades Principais</h2>
<p>O aplicativo oferece um conjunto de funcionalidades para o usuário realizar transações financeiras de forma simples e eficiente:</p>
<ul>
  <li>Depósito: Permite ao usuário adicionar fundos à sua conta bancária fictícia.</li>
  <li>Extrato: Exibe o histórico de transações, incluindo depósitos, recargas e transferências.</li>
  <li>Recarga: Possibilidade de recarregar o saldo do celular diretamente do aplicativo.</li>
  <li>Transferência: Facilita a transferência de fundos entre contas.</li>
</ul>
<h2>Arquitetura do Projeto</h2>
<p>O projeto segue a arquitetura MVVM (Model-View-ViewModel), que separa a lógica de negócios da interface do usuário, tornando o código mais modular e fácil de manter:</p>
<div>
  <h3>Model</h3>
  <p>Camada responsável por gerenciar os dados e as regras de negócio do aplicativo. Interage diretamente com as fontes de dados, como o Firebase Realtime Database.</p>
  <h3>View</h3>
  <p>A camada de interface do usuário, responsável por exibir os dados e capturar as interações dos usuários.</p>
  <h3>ViewModel</h3>
  <p>Age como um intermediário entre a View e o Model. Ele prepara os dados para serem exibidos pela View e também lida com eventos de interface do usuário.</p>
</div>
<h2>Implementações Utilizadas</h2>
<p>Abaixo estão listadas as principais implementações utilizadas no projeto:</p>
<ul>
  <li><strong>Firebase</strong>: Autenticação, Realtime Database e Storage.</li>
  <li><strong>Lifecycle</strong>: Gerenciamento de ciclos de vida, ViewModel e LiveData.</li>
  <li><strong>Hilt</strong>: Injeção de dependências simplificada.</li>
  <li><strong>MaskedEditText</strong>: Entrada de texto mascarada, ideal para formatação de campos como CPF e números de telefone.</li>
  <li><strong>ShapedImageView</strong>: Exibição de imagens com formas personalizadas.</li>
  <li><strong>Picasso</strong>: Carregamento e cache de imagens de forma eficiente.</li>
  <li><strong>TedPermission</strong>: Gerenciamento simplificado de permissões no Android.</li>
  <li><strong>SimpleSearchView</strong>: Barra de busca customizada para pesquisa rápida dentro do aplicativo.</li>
</ul>
<h2>Configurações e Permissões</h2>
<p>O aplicativo requer as seguintes permissões e configurações para funcionar corretamente:</p>
<ul>
  <li>Permissão para uso da câmera: <code>&lt;uses-permission android:name="android.permission.CAMERA"/&gt;</code></li>
  <li>Permissão para leitura de mídia: <code>&lt;uses-permission android:name="android.permission.READ_MEDIA_IMAGES"/&gt;</code></li>
  <li>Permissão para acesso à internet: <code>&lt;uses-permission android:name="android.permission.INTERNET"/&gt;</code></li>
</ul>
<h2>Documentação Técnica</h2>
<p>A seguir está um resumo das principais dependências e frameworks utilizados no projeto:</p>
<pre>
  
// Firebase
implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
implementation("com.google.firebase:firebase-auth")
implementation("com.google.firebase:firebase-database")
implementation("com.google.firebase:firebase-storage")

// Lifecycle
val lifecycle_version = "2.8.3"
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

// Hilt
implementation("com.google.dagger:hilt-android:2.51.1")
kapt("com.google.dagger:hilt-android-compiler:2.51.1")

// MaskedEditText
implementation("io.github.vicmikhailau:MaskedEditText:5.0.2")

// ShapedImageView
implementation("io.woong.shapedimageview:shapedimageview:1.4.3")

// Picasso
implementation("com.squareup.picasso:picasso:2.8")

// TedPermission
implementation("io.github.ParkSangGwon:tedpermission-normal:3.3.0")

// SimpleSearchView
implementation("com.github.Ferfalk:SimpleSearchView:0.2.1")
</pre>
<h2>Estrutura de Diretórios</h2>
<p>A estrutura de diretórios do projeto é organizada para refletir a arquitetura MVVM e seguir as melhores práticas de desenvolvimento Android:</p>
<pre>
- app/
  - src/
    - main/
      - java/
        - com/
          - yourcompany/
            - bancoDigital/
              - model/
              - view/
              - viewmodel/
      - res/
        - layout/
        - values/
</pre>
<h2>Considerações Finais</h2>
<p>Este aplicativo demonstra o uso de uma arquitetura sólida e diversas ferramentas modernas para criar um banco digital fictício no Android. As práticas adotadas garantem que o código seja limpo, organizado e facilmente escalável para futuras melhorias.</p>
