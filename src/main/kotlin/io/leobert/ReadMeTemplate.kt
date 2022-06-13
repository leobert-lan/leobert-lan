package io.leobert

fun createReadMe(
  githubActivity: List<ActivityItem>,
  blogActivity: List<ActivityItem>
): String {
  return """
    ### Hi 👋

    <!--
    **leobert-lan/leobert-lan** is a ✨ _special_ ✨ repository because its `README.md` (this file) appears on your GitHub profile.

    Here are some ideas to get you started:

    - 🔭 I’m currently working on ...
    - 🌱 I’m currently learning ...
    - 👯 I’m looking to collaborate on ...
    - 🤔 I’m looking for help with ...
    - 💬 Ask me about ...
    - 📫 How to reach me: ...
    - 😄 Pronouns: ...
    - ⚡ Fun fact: ...
    -->

    - 🔭 I’m currently working on Android application & Java server bundles
    - 📫 How to reach me: [juejin.im](https://juejin.cn/user/2066737589654327)
    - 👀 Find my blogs: [github.io](https://leobert-lan.github.io/)


    ![leobert's github stats](https://github-readme-stats.vercel.app/api?username=leobert-lan&show_icons=true&count_private=true)

    <table><tr><td valign="top" width="60%">

    ## GitHub Activity
    <!-- githubActivity starts -->
${githubActivity.joinToString("\n\n") { "    $it" }}
    <!-- githubActivity ends -->
    </td><td valign="top" width="40%">

    ## On My Blog
    <!-- blog starts -->
${blogActivity.joinToString("\n\n") { "    $it" }}
    <!-- blog ends -->
    _More on [gitbook](https://leobert-lan.github.io/)_
    </td></tr></table>
    
    <sub><a href="https://simonwillison.net/2020/Jul/10/self-updating-profile-readme/">Inspired by Simon Willison's auto-updating profile README.</a></sub>
  """.trimIndent()
}