<#assign contextPath=request.getContextPath()>
<#assign title="profile_index">
<#assign action="profile_index">
<#include '/common/mobile_header.ftl' >
<@mobileLayout>
<div class="page-settings">
    <div class="list-block media-list person-card">
        <ul>
            <li>
                <div href="#" class="item-content">
                    <div class="item-media"><img src="http://gqianniu.alicdn.com/bao/uploaded/i4//tfscom/i3/TB10LfcHFXXXXXKXpXXXXXXXXXX_!!0-item_pic.jpg_250x250q60.jpg" width="80"></div>
                    <div class="item-inner">
                        <div class="item-title-row">
                            <div class="item-title">Mr Potato</div>
                        </div>
                        <div class="item-subtitle">Time is money</div>
                        <div class="item-text"><strong>528</strong> Follow</div>
                    </div>
                </div>
            </li>
        </ul>
    </div>
    <div class="row text-center">
        <div class="col-25">
            <h4>12</h4>
            <div class="color-gray">in Cart</div>
        </div>
        <div class="col-25">
            <h4>5</h4>
            <div class="color-gray">Sent</div>
        </div>
        <div class="col-25">
            <h4>2</h4>
            <div class="color-gray">Delivery</div>
        </div>
        <div class="col-25">
            <h4>85</h4>
            <div class="color-gray">Comments</div>
        </div>
    </div>
    <div class="list-block list">
        <ul>
            <li class="item-content item-link">
                <div class="item-media"><i class="icon icon-settings"></i></div>
                <div class="item-inner">
                    <div class="item-title">Account</div>
                </div>
            </li>
            <li class="item-content item-link">
                <div class="item-media"><i class="icon icon-me"></i></div>
                <div class="item-inner">
                    <div class="item-title">Me</div>
                </div>
            </li>
            <li class="item-content item-link">
                <div class="item-media"><i class="icon icon-message"></i></div>
                <div class="item-inner">
                    <div class="item-title">Noti</div>
                </div>
            </li>
            <li class="item-content item-link">
                <div class="item-media"><i class="icon icon-star"></i></div>
                <div class="item-inner">
                    <div class="item-title">Fav</div>
                </div>
            </li>
            <li class="item-content item-link">
                <div class="item-media"><i class="icon icon-friends"></i></div>
                <div class="item-inner">
                    <div class="item-title">Help</div>
                </div>
            </li>
        </ul>
    </div>
    <div class="content-block">
        <a href="/examples/light7-mall/index.html" class="button button-big button-fill button-danger external">Logout</a>
    </div>
</div>
</@mobileLayout>
<script>
    $(document).ready(function(){
        
    })
</script>