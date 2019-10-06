
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
        $('#fmAcceptProject').submit();
    });

    /**
     * Submit topic
     */
    $('#fmBtnSubmitTopic').click(function (e){
        $('#fmSubmitTopic').submit();
    });

    /**
     * Review topic
     */
    $('#fmBtnReviewTopic').click(function (e){
        $('#fmReviewTopic').submit();
    });

    /**
     * Submit proposal
     */
    $('#fmBtnSubmitProposal').click(function (e){
        $('#fmSubmitProposal').submit();
    });

    /**
     * Review proposal
     */
    $('#fmBtnReviewProposal').click(function (e){
        $('#fmReviewProposal').submit();
    });

    /**
     * Submit research method
     */
    $('#fmBtnSubmitResearchMethod').click(function (e){
        $('#fmSubmitResearchMethod').submit();
    });

    /**
     * Review research method
     */
    $('#fmBtnReviewResearchMethod').click(function (e){
        $('#fmReviewResearchMethod').submit();
    });

    /**
     * Submit research data
     */
    $('#fmBtnSubmitResearchData').click(function (e) {
        $('#fmSubmitResearchData').submit();
    });

    /**
     * Review research data
     */
    $('#fmBtnReviewResearchData').click(function (e) {
        $('#fmReviewResearchData').submit();
    });

    /**
     * Submit analysis data
     */
    $('#fmBtnSubmitAnalysisData').click(function (e) {
        $('#fmSubmitAnalysisData').submit();
    });

    /**
     * Review data result
     */
    $('#fmBtnReviewResearchDataResult').click(function (e) {
        $('#fmReviewResearchDataResult').submit();
    });

    /**
     * Submit conclusion
     */
    $('#fmBtnSubmitConclusion').click(function (e) {
        $('#fmSubmitConclusion').submit();
    });

    /**
     * Review conclusion
     */
    $('#fmBtnReviewConclusion').click(function (e) {
        $('#fmReviewConclusion').submit();
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
                break;
            case 'submitTopic':
                $('#fmSubmitTopic #submitTopicTaskId').val(taskId);
                $('#submitTopicModal').modal('show');
                break;
            case 'reviewTopic':
                // Get text
                $('#fmReviewTopic #reviewTopicTaskId').val(taskId);
                $.ajax({
                    type: 'GET',
                    url: '/data/topic/content/'+taskId,
                    contentType: false,
                    cache: false,
                    processData: false,
                    beforeSend: function(){

                    },
                    success: function(obj){
                        $('#topicContent').text(obj);
                        $('#reviewTopicModal').modal('show');
                    }
                });
                break;
            case 'submitProposal':
                // Submit the proposal
                $('#fmSubmitProposal #submitProposalTaskId').val(taskId);
                $('#submitProposalModal').modal('show');
                break;
            case 'reviewProposal':
                // Review proposal
                $('#fmReviewProposal #reviewProposalTaskId').val(taskId);
                $.ajax({
                    type: 'GET',
                    url: '/data/proposal/content/'+taskId,
                    contentType: false,
                    cache: false,
                    processData: false,
                    beforeSend: function(){

                    },
                    success: function(obj){
                        $('#proposalContent').text(obj);
                        $('#reviewProposalModal').modal('show');
                    }
                });
                break;
            case 'submitResearchMethod':
                // Submit research method
                $('#fmSubmitResearchMethod #submitResearchMethodTaskId').val(taskId);
                $('#submitResearchMethodModal').modal('show');
                break;
            case 'reviewResearchMethod':
                // Review research method
                $('#fmReviewResearchMethod #reviewResearchMethodTaskId').val(taskId);
                $.ajax({
                    type: 'GET',
                    url: '/data/researchmethod/content/'+taskId,
                    contentType: false,
                    cache: false,
                    processData: false,
                    beforeSend: function(){

                    },
                    success: function(obj){
                        $('#researchMethodContent').text(obj);
                        $('#reviewResearchMethodModal').modal('show');
                    }
                });
                break;
            case 'collectData':
                // Submit collected data
                $('#fmSubmitResearchData #submitResearchDataTaskId').val(taskId);
                $('#submitResearchDataModal').modal('show');
                break;
            case 'reviewData':
                $('#fmReviewResearchData #reviewResearchDataTaskId').val(taskId);
                $.ajax({
                    type: 'GET',
                    url: '/data/researchdata/content/'+taskId,
                    contentType: false,
                    cache: false,
                    processData: false,
                    beforeSend: function(){

                    },
                    success: function(obj){
                        $('#researchDataContent').text(obj);
                        $('#reviewResearchDataModal').modal('show');
                    }
                });
                break;
            case 'analysisData':
                // Submit analysis data
                $('#fmSubmitAnalysisData #submitAnalysisDataTaskId').val(taskId);
                $('#submitAnalysisDataModal').modal('show');
                break;
            case 'reviewDataResult':
                // Review research data result
                $('#fmReviewResearchDataResult #reviewResearchDataResultTaskId').val(taskId);
                $.ajax({
                    type: 'GET',
                    url: '/data/researchdataresult/content/'+taskId,
                    contentType: false,
                    cache: false,
                    processData: false,
                    beforeSend: function(){

                    },
                    success: function(obj){
                        $('#researchDataResultContent').text(obj);
                        $('#reviewResearchDataResultModal').modal('show');
                    }
                });
                break;
            case 'discussAndConclusion':
                // Submit conclusion
                $('#fmSubmitConclusion #submitConclusionTaskId').val(taskId);
                $('#submitConclusionModal').modal('show');
                break;
            case 'reviewConclusion':
                // Review research data result
                $('#fmReviewConclusion #reviewConclusionTaskId').val(taskId);
                $.ajax({
                    type: 'GET',
                    url: '/data/conclusion/content/'+taskId,
                    contentType: false,
                    cache: false,
                    processData: false,
                    beforeSend: function(){

                    },
                    success: function(obj){
                        $('#conclusionContent').text(obj);
                        $('#reviewConclusionModal').modal('show');
                    }
                });
                break;
            case 'submitFirstDraft':
                break;
            case 'reviewFirstDraft':
                break;
            case 'submit2ndDraft':
                break;
            case 'review2ndDraft':
                break;
            case 'submitFinal':
                break;
            case 'reviewFinal':
                break;
            case 'defense':
                break;
            default:
                // Do nothing
        }

    })
});