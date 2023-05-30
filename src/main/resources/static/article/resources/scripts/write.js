const writeForm = document.getElementById('writeForm');

ClassicEditor.create(writeForm['content'], {
    simpleUpload: {
        uploadUrl: '/article/uploadImage'
    }

});