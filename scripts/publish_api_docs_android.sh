#!/usr/bin/env bash
set -euo pipefail

usage() {
    echo "Usage: " 1>&2;
    echo "$0 arguments [optional arguments]" 1>&2;
    echo "  Arguments : " 1>&2;
    echo "   -t <TAG>                       tag, e.g. v1.0.0" 1>&2;
    echo "   -p <GITHUB APP TOKEN>          access token for pull request creation" 1>&2;
    echo "  Optional arguments : " 1>&2;
    echo "   -a <GIT_REPOSITORY> path to the repository, by default uses current path" 1>&2;
    exit 1;
}

readonly DOCS_BASE_BRANCH_NAME_PRODUCTION="publisher-production"
readonly DOCS_BASE_BRANCH_NAME_STAGING="publisher-staging"
PATH_TO_REPO=$(pwd)
GITHUB_PR_ACCESS_TOKEN=""
readonly GENERATED_DOCS_FOLDER=${PATH_TO_REPO}/documentation
readonly DOCS_PREFIX="libjava"

while getopts "t:a:p:" opt; do
    case "${opt}" in
        t)
            TAG=${OPTARG}
            VERSION_NUMBER="${TAG//v}"
            BRANCH_NAME="docs-${TAG}"
            ;;
        p)
            GITHUB_PR_ACCESS_TOKEN=${OPTARG}
            ;;
        a)
            PATH_TO_REPO=${OPTARG}
            ;;
        *)
            usage
            ;;
    esac
done

echo "Repository path=${PATH_TO_REPO}"

checkIfGithubTokensAreProvided() {
    if [ -z "$GITHUB_PR_ACCESS_TOKEN" ]; then
        usage
    fi
}

checkIfTagExist() {
    pushd "${PATH_TO_REPO}"
    if [ "$(git tag --points-at HEAD)" = "$TAG" ]; then
        echo "tag ${TAG} exist"
    else
        echo "current commit doesn't point at the tag ${TAG}"
        usage
    fi
    echo "VERSION_NUMBER=${VERSION_NUMBER}"

    popd > /dev/null
}

generateDocs() {
    pushd "${PATH_TO_REPO}"
    echo "Start generating docs"

    make javadoc

    echo "Finish generating docs"
    popd > /dev/null
}

checkoutToPublisherStaging() {
    pushd "${PATH_TO_REPO}"

    echo "checkout to ${DOCS_BASE_BRANCH_NAME_STAGING}"
    git checkout "${DOCS_BASE_BRANCH_NAME_STAGING}"

    popd > /dev/null
}

checkoutToPublisherProduction() {
    pushd "${PATH_TO_REPO}"

    echo "checkout to ${DOCS_BASE_BRANCH_NAME_PRODUCTION}"
    git checkout "${DOCS_BASE_BRANCH_NAME_PRODUCTION}"

    popd > /dev/null
}

copyDocumentsToDocsFolders() {
    for fullPath in `find $GENERATED_DOCS_FOLDER -type d -maxdepth 1 -mindepth 1 | grep $DOCS_PREFIX` ; do
        docsDirectory=`basename $fullPath`
        targetDirectory=$PATH_TO_REPO/$docsDirectory/$VERSION_NUMBER
        mkdir -p $targetDirectory
        cp -r $fullPath/javadoc/. $targetDirectory
    done
}

pushDocsToStaging() {
    pushd "${PATH_TO_REPO}"

    echo "push docs to staging"
    git add "./$DOCS_PREFIX*"
    git commit -m "Docs ${TAG}" -q
    git push origin HEAD --set-upstream -q

    popd > /dev/null
}

createBranchProduction() {
    pushd "${PATH_TO_REPO}"

    echo "create a docs brach"
    git checkout -b ${BRANCH_NAME}
    git add "./$DOCS_PREFIX*"
    git commit -m "Docs ${TAG}" -q
    git push origin HEAD --set-upstream -q
    popd > /dev/null
}

createPullRequest() {
    pushd "${PATH_TO_REPO}"

    echo "create pull request"

    GITHUB_TOKEN=$GITHUB_PR_ACCESS_TOKEN gh pr create \
        --title "Docs ${TAG}" \
        --body "**Docs ${TAG}**
        <br/>**Staging**:
        <br/> mapbox-java-core:
        <a href='https://github.com/mapbox/mapbox-java/tree/publisher-staging/libjava-core/${VERSION_NUMBER}'>staging github</a>
        <a href='https://docs.tilestream.net/android/java/api/libjava-core/${VERSION_NUMBER}'>staging docs</a>
        <br/>mapbox-java-geojson:
        <a href='https://github.com/mapbox/mapbox-java/tree/publisher-staging/libjava-geojson/${VERSION_NUMBER}'>staging github</a>
        <a href='https://docs.tilestream.net/android/java/api/libjava-geojson/${VERSION_NUMBER}'>staging docs</a>
        <br/>mapbox-java-turf:
        <a href='https://github.com/mapbox/mapbox-java/tree/publisher-staging/libjava-turf/${VERSION_NUMBER}'>staging github</a>
        <a href='https://docs.tilestream.net/android/java/api/libjava-turf/${VERSION_NUMBER}'>staging docs</a>
        <br/>mapbox-java-services:
        <a href='https://github.com/mapbox/mapbox-java/tree/publisher-staging/libjava-services/${VERSION_NUMBER}'>staging github</a>
        <a href='https://docs.tilestream.net/android/java/api/libjava-services/${VERSION_NUMBER}'>staging docs</a>
        <br/>**cc** @mapbox/navigation-android" \
        --base ${DOCS_BASE_BRANCH_NAME_PRODUCTION} \
        --head ${BRANCH_NAME} \
        --label "Documentation"

    popd > /dev/null
}

checkIfGithubTokensAreProvided
checkIfTagExist
generateDocs
# staging
checkoutToPublisherStaging
copyDocumentsToDocsFolders
pushDocsToStaging
# production
checkoutToPublisherProduction
copyDocumentsToDocsFolders
createBranchProduction
createPullRequest