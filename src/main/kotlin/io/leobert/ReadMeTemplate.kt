package io.leobert

fun createReadMe(
  githubActivity: List<ActivityItem>,
  blogActivity: List<ActivityItem>
): String {
  return """
    ### Hi š

    <!--
    **leobert-lan/leobert-lan** is a āØ _special_ āØ repository because its `README.md` (this file) appears on your GitHub profile.

    Here are some ideas to get you started:

    - š­ Iām currently working on ...
    - š± Iām currently learning ...
    - šÆ Iām looking to collaborate on ...
    - š¤ Iām looking for help with ...
    - š¬ Ask me about ...
    - š« How to reach me: ...
    - š Pronouns: ...
    - ā” Fun fact: ...
    -->

    - š­ Iām currently working on Android application & Java server bundles
    - š« How to reach me: [juejin.im](https://juejin.cn/user/2066737589654327)
    - š Find my blogs: [github.io](https://leobert-lan.github.io/)


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