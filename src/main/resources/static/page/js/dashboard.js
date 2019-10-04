
$(function(){


    $('#newProjectModal').on('hidden.bs.modal', function (e){
        $('#projectName').val('');
        $('#projectDesc').val('');
        $('#projectDueDate').val('');
    });

    $('#acceptProjectInviteModal').on('hidden.bs.modal', function(e){
        $('#acceptInvitationComment').val('');
    });


    /**
     * Create new project
     */
    $('#fmBtnNewProject').click(function (e){
        $('#fmNewProject').submit();
    });

    /**
     * Assign students
     */
    $('#fmBtnAssignStudent').click(function (e){
        $('#fmAssignStudents').submit();
    });

    /**
     * Accept project
     */
    $("#fmBtnAcceptProject").click(function (e){
        $('#fmAcceptProject').submit;
    });




    // Basic with search
    $('.select2').select2({
        placeholder: 'Choose one',
        searchInputPlaceholder: 'Search options',
        dropdownParent: $('#assignStudentModal')
    });

    $('.btnAssignUser').click(function (e){
        e.preventDefault();
        var projectId = $(this).attr("data");
        $('#assignStudentsProjectId').val(projectId);
        $('#assignStudentModal').modal('show');
        return false;
    });

    $('.btnTaskAction').click(function (e){
        e.preventDefault();
        var taskId = $(this).closest('nav').attr('data');
        var taskKey = $(this).attr('data');

        switch (taskKey){
            case 'acceptProject':
                $('#fmAcceptProject #acceptProjectTaskId').val(taskId);
                $('#acceptProjectInviteModal').modal('show');

            default:
                // Do nothing
        }

    })
});