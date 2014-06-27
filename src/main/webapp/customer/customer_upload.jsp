<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<div id="customer-add-form-container" title="Upload Customer">
	<div class="ui-content form-panel full-content">
		 <form id="customer-upload-form" method="post" action="uploadservlet" enctype="multipart/form-data">
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 100px; width:375px !important;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_BUSINESS_NAME_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory" name="businessName" id="businessName" style="width:140px !important;">
							<input type="hidden" name="organizationId" id="organizationId"/>
						</div>
						<span id="businessNameValid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="businessName_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.BUSINESS_NAME_NOT_AVAILABLE)%></p>
							</div>
						</div>
					</div>
					<div id="business-name-suggestions" class="business-name-suggestions"></div>

					<div class="form-row">
						<div class="label">Choose file:</div>
						<div class="input-field">
							<input type="file" name="fileUpload" id="fileUpload" style="width:200px !important;">
						</div>
					</div>
					<input id="upload" type="submit" value="Upload" class="btn-upload" style="margin-left:190px; margin-top:10px;"/>
				</div>
			</div>
		</form> 
	</div>
</div>

 <script type="text/javascript">
 $('.helppop').hide();
CustomerHandler.manageUploadOperations();
</script>