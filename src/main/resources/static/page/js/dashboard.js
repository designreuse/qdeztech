
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
     * Assign students
     */
    $('#fmBtnAssignStudent').click(function (e){
        $('#fmAssignStudents').submit();
    })




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
    })
});