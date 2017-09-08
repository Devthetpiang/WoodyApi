package com.xavey.woody.api;

import com.facebook.Profile;
import com.xavey.woody.api.model.API_Response;
import com.xavey.woody.api.model.Auth;
import com.xavey.woody.api.model.Categories;
import com.xavey.woody.api.model.Comment;
import com.xavey.woody.api.model.Comments;
import com.xavey.woody.api.model.FBUser;
import com.xavey.woody.api.model.Followers;
import com.xavey.woody.api.model.Like;
import com.xavey.woody.api.model.NotificationResult;
import com.xavey.woody.api.model.Post;
import com.xavey.woody.api.model.PostResult;
import com.xavey.woody.api.model.PostSet;
import com.xavey.woody.api.model.PostSetResult;
import com.xavey.woody.api.model.PostSets;
import com.xavey.woody.api.model.Posts;
import com.xavey.woody.api.model.PromoPosts;
import com.xavey.woody.api.model.Reward;
import com.xavey.woody.api.model.Rewards;
import com.xavey.woody.api.model.User;
import com.xavey.woody.api.model.UserLike;
import com.xavey.woody.api.model.UserReferrals;
import com.xavey.woody.api.model.UserRelated;
import com.xavey.woody.api.model.Users;
import com.xavey.woody.api.model.Version;
import com.xavey.woody.api.model.Vote;
import com.xavey.woody.api.model.VoteSet;

import java.util.Date;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;

/**
 *
 * Created by tinmaungaye on 23.10.14.
 */
public interface WoodyApiInterface {

    @GET("/api/version")
    void getVersion(Callback<Version> result);

    @FormUrlEncoded
    @POST("/api/oauth/token")
    void postAuthToken(@Field("grant_type") String granttype,
                       @Field("client_id") String clientid,
                       @Field("client_secret") String clientsecret,
                       @Field("username") String username,
                       @Field("password") String password,
                       Callback<Auth> result);


    @POST("/api/oauth/facebook/mobile")
    void postAuthTokenFB(@Body FBUser UserJSON, Callback<Auth> result);

    @Multipart
    @POST("/api/users/fullprofile/")
    void postFullProfile(@Header("Authorization")String authorization,
                         @Part("uploaded_file") TypedFile file,
                         @Part("full_name") String full_name,
                         @Part("phone") String phone,
                         @Part("gender") String gender,
                         @Part("dob") Date dob,
                         @Part("nric") String nric,
                         @Part("current_city") String current_city,
                         @Part("income") String income,
                         @Part("education") String education,
                         @Part("facebook_id") String facebook_id,
                         @Part("facebook_token") String facebook_token,
                       Callback<API_Response> result);

    @Multipart
    @POST("/api/users/fullprofile/")
    void postFullProfile(@Header("Authorization")String authorization,
                         @Part("full_name") String full_name,
                         @Part("phone") String phone,
                         @Part("gender") String gender,
                         @Part("dob") Date dob,
                         @Part("nric") String nric,
                         @Part("current_city") String current_city,
                         @Part("income") String income,
                         @Part("education") String education,
                         Callback<API_Response> result);

    //basic registration
    @POST("/api/users/reg/{referalName}")
    void postNewUser(@Body User UserJSON, @Path("referalName")String referalName, Callback<API_Response> result);

    //full profile request
    @GET("/api/users/fullinfo/{userID}/")
    void getUserFullProfile(@Header("Authorization")String authorization,
                            @Path("userID")String userID,
                            Callback<UserRelated> result);

    @GET("/api/users/info/{userID}/")
    void getUserRelatedStat(@Header("Authorization")String authorization,
                            @Path("userID")String userID,
                            Callback<UserRelated> result);

    @GET("/api/users/mp/")
    void getUserMellPoint(@Header("Authorization")String authorization,
                            Callback<UserRelated> result);

    @GET("/api/users/noti/")
    void getUserRelatedNoti(@Header("Authorization")String authorization,
                            Callback<NotificationResult> result);

    @POST("/api/users/forgot")
    void postGenToken(@Body User UserJSON, Callback<API_Response> result);

    @POST("/api/users/reset")
    void postResetPassword(@Body User UserJSON, Callback<API_Response> result);

    @POST("/api/posts/")
    void postNewPost(@Header("Authorization")String authorization,@Body Post PostJSON, Callback<API_Response> result);

    @Multipart
    @POST("/api/posts/image/")
    void newPostWithImage(@Header("Authorization")String authorization,@Part("uploaded_file") TypedFile image,@Part("title")String title,
                          @Part("item1") String item1,@Part("item2")String item2,@Part("item3") String item3,@Part("item4")String item4,@Part("item5")String item5,
                          @Part("category1") String category1,@Part("category2")String category2,@Part("category3") String category3, Callback<API_Response> result);


    @GET("/api/categories/")
    void getCategories(@Header("Authorization")String authorization, Callback<Categories> result);

    @GET("/api/posts/surprise/{noItem}/{skipItem}")
    void getSurprise(@Header("Authorization")String authorization, @Path("noItem")String noItem, @Path("skipItem")String skipItem, Callback<Post> result);

    @POST("/api/posts/{postID}/vote/")
    void postAVote(@Header("Authorization")String authorization,
                     @Path("postID")String postID,
                     @Body Vote VoteJSON, Callback<API_Response> result);

    @POST("/api/posts/{postID}/skipvote/")
    void postASkipVote(@Header("Authorization")String authorization,
                   @Path("postID")String postID,
                   @Body Vote VoteJSON, Callback<API_Response> result);

    @GET("/api/posts/{postID}/result/")
    void getVoteResults(@Header("Authorization")String authorization,
                   @Path("postID")String postID,
                   Callback<PostResult> result);

    @POST("/api/posts/{postID}/comment/")
    void postAComment(@Header("Authorization")String authorization,
                   @Path("postID")String postID,
                   @Body Comment CommentJSON, Callback<API_Response> result);

    @GET("/api/posts/{postID}/comment/{page}")
    void getComment(@Header("Authorization")String authorization,
                      @Path("postID")String postID,
                      @Path("page")int pagi, Callback<Comments> result);

    @GET("/api/posts/search/{type}/{userID}/")
    void getUserPost(@Header("Authorization")String authorization,
                     @Path("type")String searchType,
                    @Path("userID")String postID, Callback<Posts> result);

    @GET("/api/users/search/{query}/")
    void getUserSearch(@Header("Authorization")String authorization,
                     @Path("query")String query, Callback<Users> result);

    @GET("/api/users/partners/{queryId}/")
    void getUserPartners(@Header("Authorization")String authorization,
                       @Path("queryId")String queryId, Callback<Users> result);

    @GET("/api/users/following/{query}/")
    void getUserFollowing(@Header("Authorization")String authorization,
                       @Path("query")String query, Callback<Followers> result);

    @GET("/api/users/follower/{query}/")
    void getUserFollower(@Header("Authorization")String authorization,
                       @Path("query")String query, Callback<Followers> result);

    @POST("/api/posts/{postID}/like/")
    void postALike(@Header("Authorization")String authorization,
                   @Path("postID")String postID,
                   @Body Like LikeJSON, Callback<API_Response> result);

    @DELETE("/api/posts/{postID}/like/{likedID}")
    void deleteALike(@Header("Authorization")String authorization,
                   @Path("postID")String postID,
                   @Path("likedID")String likedID, Callback<API_Response> result);

    @POST("/api/users/{userID}/like/")
    void postAUserLike(@Header("Authorization")String authorization,
                   @Path("userID")String userID,
                   @Body UserLike LikeJSON, Callback<API_Response> result);

    @DELETE("/api/users/{userID}/like/{likedID}")
    void deleteAUserLike(@Header("Authorization")String authorization,
                     @Path("userID")String userID,
                     @Path("likedID")String likedID, Callback<API_Response> result);

    @POST("/api/users/syncmeller/contacts/")
    void postSyncMeller(@Header("Authorization")String authorization,
                       @Body String[] phoneNos, Callback<UserReferrals> result);

    @POST("/api/users/refmeller/contacts/")
    void postRefMeller(@Header("Authorization")String authorization,
                       @Body String[] mellerNo, Callback<API_Response> result);

    @PUT("/api/posts/{postID}/report/")
    void putAFlag(@Header("Authorization")String authorization,
                   @Path("postID")String postID,
                   @Body Vote vPost, Callback<API_Response> result);

    @GET("/api/postsets/{createdBy}/")
    void getPostSetList(@Header("Authorization")String authorization,@Path("createdBy")String noItem, Callback<PostSets>results);

    @GET("/api/postsets/selected/{postsetId}/")
    void getPostSet(@Header("Authorization")String authorization,@Path("postsetId")String noItem, Callback<PostSet>results);

    @POST("/api/postsets/{postsetId}/voteset/")
    void postVoteSet(@Header("Authorization")String authorization,
                     @Path("postsetId")String postsetId,
                     @Body VoteSet voteSet,Callback<API_Response>result);

    @GET("/api/postsets/{postsetId}/result/")
    void getVoteSetResults(@Header("Authorization")String authorization,
                           @Path("postsetId")String postsetId,
                           Callback<PostSetResult>result);
    @GET("/api/postsets/")
    void getPromotionItem(@Header("Authorization")String authorization,Callback<PromoPosts>result);

    @GET("/api/rewards/showcase/")
    void getRewards(@Header("Authorization")String authorization,
                            Callback<Rewards> result);

    @POST("/api/rewards/enroll/")
    void postRewardEnroll(@Header("Authorization")String authorization,
                     @Body Reward reward,Callback<API_Response>result);

    @POST("/api/users/invited/")
    void postInviteCounter(@Header("Authorization")String authorization,
                          @Body String dummy,Callback<API_Response>result);

}
