<script>
    import appConfig from "$lib/global/appConfig";

    // 리포지토리 목록을 저장할 상태
    let articlesRs = $state([]);
    // 컴포넌트 마운트 시 GitHub API로부터 데이터 가져오기

    $effect(async () => {
        const response = await fetch(`${appConfig.getCoreApiUrl()}/articles/notice1`);
        articlesRs = await response.json();
    });
</script>

<svelte:head>
    <title>Home</title>
    <meta name="description" content="홈 화면"/>
</svelte:head>

<div>
    {JSON.stringify(articlesRs)}
</div>