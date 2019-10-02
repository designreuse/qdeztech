
$(function(){


    $('#newProjectModal').on('hidden.bs.modal', function (e){
        $('#projectName').val('');
        $('#projectDesc').val('');
        $('#projectDueDate').val('');
    });


    /**
     * Create new project
     */
    $('#fmBtnNewProject').click(function (e){
        $('#fmNewProject').submit();
    });


    /**
     * Kick off the BPMN process
     */
    $('#fmNewProject').on('submit', function (e){
        e.preventDefault();

        // Ajax call
        $.ajax({
            type: 'POST',
            url: '/api/startproject',
            data: new FormData(this),
            contentType: false,
            cache: false,
            processData: false,
            beforeSend: function(){

            },
            success: function(msg){
                $('#newProjectModal').modal('hide');
            }
        })

    })
});