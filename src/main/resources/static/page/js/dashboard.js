
$(function(){

    var ulRow = _.template('<li class="list-group-item d-flex pd-sm-x-20 projectList">' +
        '                                <div class="avatar d-none d-sm-block"><span class="avatar-initial rounded-circle bg-teal"><i class="icon ion-md-checkmark"></i></span></div>' +
        '                                <div class="pd-sm-l-10">' +
        '                                    <p class="tx-medium mg-b-0"><% print(obj.projectName) %></p>' +
        '                                    <small class="tx-12 tx-color-03 mg-b-0"><% print("Due Date: " + obj.projectDueDate) %></small>' +
        '                                </div>' +
        '                                <div class="mg-l-auto text-right">' +
        '                                    <p class="tx-medium mg-b-0"><% print("Students: " + obj.students) %></p>' +
        '                                    <small class="tx-12 tx-danger mg-b-0"><% print("Current Step: " + obj.currentStage) %></small>' +
        '                                </div>' +
        '                               <div class="mg-l-20-f d-flex align-self-center">' +
        '                                    <nav class="nav nav-icon-only">' +
        '                                        <a href="" class="text-danger nav-link d-none d-sm-block"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-slash"><circle cx="12" cy="12" r="10"></circle><line x1="4.93" y1="4.93" x2="19.07" y2="19.07"></line></svg></a>' +
        '                                        <a href="" class="text-primary nav-link d-none d-sm-block"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-user"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path><circle cx="12" cy="7" r="4"></circle></svg></a>' +
        '                                    </nav>' +
        '                                </div>'+
        '                            </li>');


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


    // /**
    //  * Kick off the BPMN process
    //  */
    // $('#fmNewProject').on('submit', function (e){
    //     e.preventDefault();
    //
    //     // Ajax call
    //     $.ajax({
    //         type: 'POST',
    //         url: '/api/startproject',
    //         data: new FormData(this),
    //         contentType: false,
    //         cache: false,
    //         processData: false,
    //         beforeSend: function(){
    //
    //         },
    //         success: function(obj){
    //             $('#newProjectModal').modal('hide');
    //
    //             // Apply template
    //
    //             $('#myProjectList').prepend(ulRow(obj));
    //         }
    //     })
    //
    // });
});