package com.xavey.woody.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.xavey.woody.R;
import com.xavey.woody.api.SampleClient;
import com.xavey.woody.api.model.API_Response;
import com.xavey.woody.api.model.User;
import com.xavey.woody.api.model.UserRelated;
import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.CircleTransform;
import com.xavey.woody.helper.DBHelper;
import com.xavey.woody.helper.DisplayManager;
import com.xavey.woody.helper.ImageSavingManager;
import com.xavey.woody.helper.PicassoHelper;
import com.xavey.woody.helper.TokenHelper;
import com.xavey.woody.helper.TypeFaceHelper;
import com.xavey.woody.helper.UtilityHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;


public class EditProfileActivity extends BaseActivity{
    final String LOG_TAG = "myLogs";
//    public static final String EXTRA_ITEM = "DetailActivity.EXTRA_ITEM";

    @InjectView(R.id.eTFullNameReg)
    EditText eTFullNameReg;

    @InjectView(R.id.eTMobileReg)
    EditText eTMobileReg;

    @InjectView(R.id.tvUserName)
    TextView tvUserName;

    @InjectView(R.id.tvGender)
    RadioGroup tvGender;

    @InjectView(R.id.rBFemaleReg)
    RadioButton rBFemaleReg;

    @InjectView(R.id.rBMaleReg)
    RadioButton rBMaleReg;

    @InjectView(R.id.dPDoBReg)
    DatePicker dPDoBReg;

    @InjectView(R.id.btnOk)
    Button btnOk;

    @InjectView(R.id.iVProfilePr)
    ImageView iVProfilePr;

    @InjectView(R.id.click)
    Button click;

    @InjectView(R.id.city_spinner)
    Spinner spinnerCity;

    @InjectView(R.id.township_spinner)
    Spinner spinnerTown;

    @InjectView(R.id.income_spinner)
    Spinner incomeSpinner;

    @InjectView(R.id.edu_spinner)
    Spinner eduSpinner;

    @InjectView(R.id.nrcNo)
    EditText nrcNo;

    @InjectView(R.id.tVMessageReg)
    TextView tVMessageReg;

    DBHelper dbHelper;
    String AuthToken ="";

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private String[] country;

    private String requestID ="me";
    DisplayManager displayManager;

    private String preIncome="Select Income Range";
    private String preEduc="Select Eduction";
    private String preCity="Select Current City";
    private String preTown="Select Current Town";

    String[]strCity ={preCity,"Yangon","Mandalay","Sagaing","Magway","Ayeyarwady","Bago(East)","Bago(West)","Tanintharyi","Kachin","Kayar","Kayin","Chin","Mon","Rakhine","Shan(South)","Shan(North)","Shan(East)"};
    String[]yangonDivision={preTown,"Taikkyi","Hlegu","Hmawbi","Htantabin","Mingaladon","Shwepyithar","Dagon Myothit(East)","North Okkalapa","Insein","Kayan","Dagon Myothit(North)","South Okkalapa","Dagon Myothit(Seikkan)","Hlaing","Yankin",
        "Thingangyu","Kamaryut","Bahan","Tamwe","Sanchaung","Kyeemyindaing","Thaketa","Thanlyin","Dagon","Mingalartaungnyunt","Dawbon","Ahlone", "Lamadaw","Pazundaung","Latha","Pabedan","Kyatktada","Botahtaung","Seikkan","Seikgyikanaungto",
        "Thongwa"," Twantay","Dala","Kyauktan","Kawhmu","Kung4","Cocokyun"};
    String[]mandalayDivision ={preTown,"Thabeikkyin","Mongoke","Singu","Madaya","Patheingyi","Aungmyaythanzan","Chanayethazan","Pyigyitagon","Amarapura","Ngazun","Sintgaing","Tada-U","Kyaukse","Myingyan","Natogyi","Myittha","Taungtha",
            "Wundwin","Mahlaing","Nyaung-U","Kyaukpadaung","Meiktila","Thazi","Pyawbwe","Yamethin","Nay Pyi Taw-Tatkon","Nay Pyi Taw-Pyimana","Nay Pyi Taw-Lewe"};
    String[]sagaingDivision={preTown,"Monywa","Nanyun","Lahe","Hkamti","LayShi","Homalin","Banmauk","Indaw","Paungbyin","Tamu","Katha","Pinlebu","Wuntho","Tigyaing","Mawlaik","Kawlin","Kyunhla","Kalewa","Kanbalu","Kale","Taze","Mingin","Ye_U","Khin_U","Tabayin","Shwebo","Kani","Budalin",
    "Wetlet","Ayadaw","Yinmabin","Myinmu","Pale","Salingyi","Chanung_U","Myaung"};
    String[]magwayDivision={preTown,"Magway","Gangaw","Tilin","Myaing","Yesagyo","Pauk","Pakokku","Saw","Seikphyu","Chauk","Salin","Sidoktaya","Natmauk","Yenangyaung","Pwintphyu","Myothit","Myothit","Minbu","Ngape","Taungdwingyi","Minhla","Sinbaungwe","Thayet","Mindon","Aunglan","Kamma"};
    String[]ayeyarwadyDivision={preTown,"Pathein","Kyangin","Myanaung","Ingapu","Hinthada","Lemyethna","Zalun","Yegyi","Kyonpyaw","DanuPhyu","Thabaung","Kyaunggon","Nyaungdon","Pantanaw","Kangyidaung","Einme","Maubin","Wakema","Kyaiklat","Myaungmya","Nagpudaw","Mawlamyinegyun","Dedaye","Bogale","Labutta","Pyapon"};
    String[]bagoEastDivision={preTown,"Taungoo","Yedashe","Oktwin","Htantabin","Phyu","Kyaukkyi","Kyauktaga","Nyaunglebin","Shwegyin","Daik_U","Bago","Waw","Thanatpin","Kawa"};
    String[]bagoWestDivision={preTown,"Pyay","Paukkhaung","Padaung","Paungde","Thegon","Shwedaung","Nattalin","Zigon","Gyobingauk","Okpho","Monyou","Minhla","Letpadan","Thayarwady"};
    String[]tanintharyiDivision={preTown,"Myeik","Yebyu","Launglon","Dawei","Thayetchaung","Palaw","Kyunsu","Bokpyin","Kawthoung"};
    String[]kachinState ={preTown,"Puta-O","Nawngmun","Machanbaw","Khaunglanpu","Tanai","Sumprabum","Tsawlaw","Injangyang","Chipwi","Hpakan","Myityina","Mogaung","Waingmaw","Monhnyin","Momauk","Shwegu","Bhamo","Mansi"};
    String[]kayarState ={preTown,"Loikaw","Shadaw","Demoso","Hpruso","Bawlakhe","Hpawawng","Mese"};
    String[]kayinState ={preTown,"Hpapun","Thandaunggyi","Hlaingbwe","Hpa-An","Myawaddy","Kyainsekgyi"};
    String[]chinState = {preTown,"Hakha","Tonzang","Tidim","Falam","Htantlang","Madupi","Mindat","Paletwa","Kanpetlet"};
    String[]monState ={preTown,"Mawlamyine","Kyaikto","Bilin","Thaton","Paung","Chaungzon","Kyaikmaraw","Mudon","Thanbyuzayat","Ye"};
    String[]rakhineState ={preTown,"Sittwe","Maungdaw","Buthidaung","Kyauktaw","Mauk-U","Ponagyun","Rathedaung","Minbya","Pauktaw","Myebon","Ann","Kyaukphyu","Ramree","Toungup","Munaung","Thandwe","Gwa"};
    String[]shanEasteState={preTown,"Kengton","Matman","Mongyaung","Mongkhet","Mongla","Mongping","Monghpyak","Mongyaung","Mongton","Monghsat","Tachileik"};
    String[]shanNorthState ={preTown,"Muse","Mabein","Mongmit","Manton","Namkan","Kutkai","Konkyaun","Laukkaing","Kunlong","Namhsan","Namtu","Hsein","Lashio","Hopang","Mongmao","Pangwaun","Nawnghkio","Kyaukme","Hsipaw","Mongyai","Tangyan","Pangsang","Namphan"};
    String[]shanSouthState ={preTown,"Taunggyi","Ywangan","Lawksawk","Mongkaung","Kyethi","Monghsu","Pindaya","Kalaw","Hopong","Loilen","Laihka","Nansang","Kunhing","Mongnai","Pinlaung","Nyaungshwe","Hsiheseng","Mawkmai","Langkho","Mongpan","Pekon","Kengtung"};
    String[]incomeSet ={preIncome,"Under 100000","100000~250000","250000~350000","350000 and Above"};
    String[]educationSet ={preEduc,"Under High Education","High School","Over High Education","Pre Graduated","Graduated","Post Graduated"};

    String selectedCity ="";
    String _id;
    String password;
    String loadedCity="";
    String loadedTown="";
    String income ="";
    String education ="";

    int position =0;
    String val;

    ArrayAdapter<String>currentDiviAdapter;

    ArrayAdapter<String>currentTownAdapter;
    ArrayAdapter<String>incomeAdapter;
    ArrayAdapter<String>eduAdapter;
    private Picasso mPicasso;
    private String toBeInvalidated="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);

        try {
            AuthToken= TokenHelper.genAPIToken(this, EditProfileActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(AuthToken!="") {
            //this.setTitle("Edit Profile");

            ButterKnife.inject(this);
            getMellPoint(this,AuthToken);

            initLoad();
            loadProfile();
        }

        dbHelper = new DBHelper(this);
    }

    public void initLoad(){
        mPicasso = PicassoHelper.getInstance(EditProfileActivity.this,AuthToken).getPicasso();
        currentDiviAdapter =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,strCity);
        currentDiviAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(currentDiviAdapter);
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCity = spinnerCity.getSelectedItem().toString();
                switch (selectedCity) {
                    case "Yangon":
                        forTown(yangonDivision);
                        break;
                    case "Mandalay":
                        forTown(mandalayDivision);
                        break;
                    case "Sagaing":
                        forTown(sagaingDivision);
                        break;
                    case "Magway":
                        forTown(magwayDivision);
                        break;
                    case "Ayeyarwady":
                        forTown(ayeyarwadyDivision);
                        break;
                    case "Bago(East)":
                        forTown(bagoEastDivision);
                        break;
                    case "Bago(West)":
                        forTown(bagoWestDivision);
                        break;
                    case "Tanintharyi":
                        forTown(tanintharyiDivision);
                        break;
                    case "Kachin":
                        forTown(kachinState);
                        break;
                    case "Kayar":
                        forTown(kayarState);
                        break;
                    case "Kayin":
                        forTown(kayinState);
                        break;
                    case "Chin":
                        forTown(chinState);
                        break;
                    case "Mon":
                        forTown(monState);
                        break;
                    case "Rakhine":
                        forTown(rakhineState);
                        break;
                    case "Shan(South)":
                        forTown(shanSouthState);
                        break;
                    case "Shan(North)":
                        forTown(shanNorthState);
                        break;
                    case "Shan(East)":
                        forTown(shanEasteState);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        incomeAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,incomeSet);
        incomeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        incomeSpinner.setAdapter(incomeAdapter);

        eduAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,educationSet);
        eduAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eduSpinner.setAdapter(eduAdapter);

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons =getResources().obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles,navMenuIcons);
    }

    public void forTown(String[]township){
        currentTownAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,township);
        currentTownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTown.setAdapter(currentTownAdapter);

        spinnerTown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            protected Adapter initializedAdapter = null;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (initializedAdapter != parent.getAdapter()) {
                    initializedAdapter = parent.getAdapter();
                    return;
                }
                val = spinnerTown.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(!loadedTown.equals("")) {
            spinnerTown.post(new Runnable() {
                @Override
                public void run() {
                    int spinnerPosition = currentTownAdapter.getPosition(loadedTown);
                    spinnerTown.setSelection(spinnerPosition);
                }
            });
        }
    }

    @OnClick(R.id.btnOk)
    public void RegistrationOK() {
        // TODO submit data to server...
        submitted();
        String strFullName = eTFullNameReg.getText().toString();
        final String strMobile = eTMobileReg.getText().toString();
//        String strPassword = tvPassword.getText().toString();
//        String strRePassword = tvRePassword.getText().toString();
        final String strUserName = tvUserName.getText().toString();
        String strGender = ""; //rGGenderReg;

        Date dtDOB = UtilityHelper.getDateFromDatePicker(dPDoBReg);
        // find the radiobutton by returned id
        RadioButton rBGender = (RadioButton) findViewById(tvGender.getCheckedRadioButtonId());
        if (rBGender != null) {
            strGender = rBGender.getText().toString();
        }

        String strNRIC =nrcNo.getText().toString();

        String strCurrentCity=spinnerCity.getSelectedItem()==null?preCity:spinnerCity.getSelectedItem().toString();
        if (strCurrentCity.equals(preCity)) {
            strCurrentCity="";
        }
        String strCurrentTown =spinnerTown.getSelectedItem()==null?preTown:spinnerTown.getSelectedItem().toString();
        if (strCurrentTown.equals(preTown)) {
            strCurrentTown="";
        }
        String incomeRange = incomeSpinner.getSelectedItem()==null?preIncome:incomeSpinner.getSelectedItem().toString();
        if(incomeRange.equals(preIncome)){
            incomeRange="";
        }
        String education = eduSpinner.getSelectedItem()==null?preEduc:eduSpinner.getSelectedItem().toString();
        if(education.equals(preEduc)){
            education="";
        }
        TypedFile typedImage;

        //TODO: Add dob checking
        if ((strUserName.equalsIgnoreCase(""))
                || (strFullName.equalsIgnoreCase("")) ||(strGender.equalsIgnoreCase(""))|| (strMobile.equalsIgnoreCase(""))){
            toBeSubmitted();
            tVMessageReg.setText("You've not filled required fields!");
            tVMessageReg.setTextColor(getResources().getColor(R.color.red_500));
        }
        else {
            User rUser = new User();
            rUser.set_id(_id);
            rUser.setFull_name(strFullName);
            rUser.setPhone(strMobile);
            rUser.setGender(strGender);
            rUser.setDob(dtDOB);
            rUser.setNric(strNRIC);
            rUser.setCurrent_city(strCurrentCity+"|"+strCurrentTown);
            rUser.setIncome(incomeRange);
            rUser.setEducation(education);
            if(imagePath !=""){
                try {
                    File photo = new File(imagePath);
                    FileOutputStream fOut = null;

                    if(photo.length()/1024>200) {
                        photo = new File(imagePath+".png");
                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                        bitmap = ImageSavingManager.getResizedBitmap(bitmap, 500);
                        fOut = new FileOutputStream(photo);
                        boolean compressed = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                        fOut.flush();
                        fOut.close();
                    }
                    typedImage= new TypedFile("application/octet-stream", photo);
                    uploadWithImage(typedImage, strFullName, strMobile,strGender, dtDOB, strNRIC, strCurrentCity, strCurrentTown, incomeRange, education);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else{
                uploadWithoutImage(strFullName,strMobile,strGender,dtDOB,strNRIC,strCurrentCity,strCurrentTown,incomeRange,education);
            }

        }

    }

    private void uploadWithoutImage(final String full_name, final String phone, final String gender,Date dob,String nric,String current_city,String current_town,String income,String education){
        SampleClient.getWoodyApiClient(this).postFullProfile(this.AuthToken, null, full_name, phone,gender, dob, nric, current_city + "|" + current_town,
                                    income, education, null, null, new Callback<API_Response>() {//change update process
            @Override
            public void success(API_Response apiRes, Response response) {
                editSuccess(apiRes, response);
            }


            @Override
            public void failure(RetrofitError error) {
                editFailure(error);
            }
        });
    }


    private void uploadWithImage(TypedFile image, final String full_name, final String phone, final String gender, Date dob,String nric,String current_city,String current_town,String income,String education){
        SampleClient.getWoodyApiClient(this).postFullProfile(this.AuthToken, image, full_name, phone, gender, dob, nric,
                    current_city + "|" + current_town, income, education, null, null, new Callback<API_Response>() {//change update process
            @Override
            public void success(API_Response apiRes, Response response) {
                try {
                    File photo = new File(imagePath + ".png");
                    if (photo.exists()) {
                        photo.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                editSuccess(apiRes, response);
            }

            @Override
            public void failure(RetrofitError error) {
                editFailure(error);
            }
        });
    }

    private void editSuccess(API_Response apiRes, Response response) {
                    if (apiRes.getMessage().equals(EditProfileActivity.this.getString(R.string.reg_code_001_mobile_no_invalid))) {
                        toBeSubmitted();
                        tVMessageReg.setText("The given mobile no is not valid.");
                        tVMessageReg.setTextColor(getResources().getColor(R.color.red_500));
                    }
                    else if (apiRes.getMessage().equals(EditProfileActivity.this.getString(R.string.reg_code_002_duplicate_user))) {
                        toBeSubmitted();
                        tVMessageReg.setText("Login  is already taken! Choose something else please.");
                        tVMessageReg.setTextColor(getResources().getColor(R.color.red_500));
                    } else if (apiRes.getMessage().equals(EditProfileActivity.this.getString(R.string.reg_code_005))) {
                        btnOk.setEnabled(true);
                        tVMessageReg.setText("Login user name  is not a valid name! Only allowed a~z 0~9 _ .");
                        tVMessageReg.setTextColor(getResources().getColor(R.color.red_500));
                    } else if (apiRes.getMessage().equals(EditProfileActivity.this.getString(R.string.reg_code_006_age_under_16))) {
                        toBeSubmitted();
                        tVMessageReg.setText("This application is for age 15 and above.");
                        tVMessageReg.setTextColor(getResources().getColor(R.color.red_500));
                    }
                    else if(apiRes.getMessage().equals(EditProfileActivity.this.getString(R.string.reg_code_007_password_length))){
                        toBeSubmitted();
                        tVMessageReg.setText("No. of Password characters must be 6 to 15");
                        tVMessageReg.setTextColor(getResources().getColor(R.color.red_500));
                    }
                    else if (apiRes.getMessage().equals(EditProfileActivity.this.getString(R.string.reg_code_008_userName_phone))) {
                        toBeSubmitted();
                        tVMessageReg.setText("Existing account found with given User Name ' userName ' or Mobile no. ' mobilephone '");
                        tVMessageReg.setTextColor(getResources().getColor(R.color.red_500));
                    }else
                     if (apiRes.getMessage().equals(EditProfileActivity.this.getString(R.string.reg_code_010_success))) {
                        tVMessageReg.setText("Welcome! Your account is edited.");
                        tVMessageReg.setTextColor(getResources().getColor(R.color.green_400));
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                        builder.setTitle("Success");
                        builder.setMessage("Your accoount has been updated.");
                         if(!toBeInvalidated.equals("")) {
                             mPicasso.invalidate(toBeInvalidated);
                         }
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                intent.putExtra(ProfileActivity.EXTRA_KILL_CACHE,true);
                                startActivity(intent);
                                finish();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        btnOk.setEnabled(true);
                        tVMessageReg.setText("Please check your entry and try again later.");
                        tVMessageReg.setTextColor(getResources().getColor(R.color.red_500));
                    }
                    Log.d("regEditUser", response.toString());
//                    toBeSubmitted();
    }

    private void editFailure(RetrofitError error){
        btnOk.setEnabled(true);
        if (error.getMessage().equals(getResources().getString(R.string.api_error_offline))) {
            tVMessageReg.setText(R.string.api_error_offline_message);
        } else {
            tVMessageReg.setText("Please check your entry and try again later.");
        }
        tVMessageReg.setTextColor(getResources().getColor(R.color.red_500));
        toBeSubmitted();
        Log.d("regNewUser", error.toString());
    }

    String token ="";
    private void loadProfile(){
        token =this.AuthToken;
        SampleClient.getWoodyApiClient(this).getUserFullProfile(this.AuthToken, requestID, new Callback<UserRelated>() {

            @Override
            public void success(UserRelated userRelated, Response response) {
                if (userRelated != null) {
                    final User user = userRelated.getUser();
                    eTFullNameReg.setText(user.getFull_name());
                    TypeFaceHelper.setM3TypeFace(eTFullNameReg, EditProfileActivity.this);
                    _id = user.get_id();
                    password = user.getHashed_password();
                    String gender = user.getGender();
                    if (gender!=null && gender.equals("Female")) {
                        rBFemaleReg.setChecked(true);
                    }
                    else if (gender!=null && gender.equals("Male")){
                        rBMaleReg.setChecked(true);
                    }
                    if(user.getPhone().indexOf(UtilityHelper.getMyFB(EditProfileActivity.this))==-1) {
                        eTMobileReg.setText(user.getPhone());
                    }
                    tvUserName.setText(user.getUser_name());

                    Calendar c = Calendar.getInstance();
                    if(user.getDob()!=null) {
                        Date dateOfBirth = user.getDob();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String newDate = sdf.format(dateOfBirth);
                        String[] date = newDate.split("/");
                        int mYear = Integer.parseInt(date[2]);
                        int mMonth = Integer.parseInt(date[1]);
                        int mday = Integer.parseInt(date[0]);

                        c.set(mYear, mMonth, mday);
                        c.add(Calendar.MONTH, -1);
                        dPDoBReg.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), null);
                    }
                    nrcNo.setText(user.getNric());
                    TypeFaceHelper.setM3TypeFace(nrcNo, EditProfileActivity.this);

                    String cityNtown = user.getCurrent_city();
                    if (cityNtown != null) {
                        String[] strarray = cityNtown.split("\\|");
                        if (strarray!=null && strarray.length==2) {
                            loadedCity = strarray[0].toString();
                            int spinnerPosition = currentDiviAdapter.getPosition(loadedCity);
                            spinnerCity.setSelection(spinnerPosition);
                            loadedTown = strarray[1].toString();
                        }
                    }

                    education = user.getEducation();
                    if (education != null) {
                        int spinnerPosition = eduAdapter.getPosition(education);
                        eduSpinner.setSelection(spinnerPosition);
                    } else if (education == null) {
                        eduAdapter = new ArrayAdapter<String>(EditProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, educationSet);
                        eduAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        eduSpinner.setAdapter(eduAdapter);
                    }

                    income = user.getIncome();
                    if (income != null) {
                        int spinnerPosition = incomeAdapter.getPosition(income);
                        incomeSpinner.setSelection(spinnerPosition);
                    } else if (income == null) {
                        incomeAdapter = new ArrayAdapter<String>(EditProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, incomeSet);
                        incomeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        incomeSpinner.setAdapter(incomeAdapter);
                    }

                    if (user.getPicture() != null) {
                        toBeInvalidated = EditProfileActivity.this.getString(R.string.api_endpoint) + EditProfileActivity.this.getString(R.string.api_endpoint_profile) + user.getPicture();
                        mPicasso.invalidate(toBeInvalidated);
                        mPicasso.load(toBeInvalidated)
                                .error(android.R.drawable.stat_notify_error)
                                .placeholder(android.R.drawable.stat_notify_sync)
                                .transform(new CircleTransform()).into(iVProfilePr);
                    } else if (user.getPicture() == null && user.getGender()!= null) {
                        switch (user.getGender().toLowerCase()) {
                            case "male":
                                iVProfilePr.setImageResource(R.drawable.ic_profile_mal);
                                break;
                            case "female":
                                iVProfilePr.setImageResource(R.drawable.ic_profile_fem);
                                break;
                        }
                    }

                    final int randomID = randInt(1, 10000);
                    final String fieldName = user.getFull_name();
                    final String fieldType = "profile";
                    final String viewId = Integer.toString(randomID);

                    ScrollView scroll = new ScrollView(getApplicationContext());
                    scroll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    final LinearLayout parentLayout = new LinearLayout(getApplicationContext());
                    parentLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                    LinearLayout.LayoutParams parentLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//                    int fivePercentWidth =displayManager.getWidth(5);
                    parentLayoutParams.setMargins(5, 0, 5, 0);
                    parentLayout.setLayoutParams(parentLayoutParams);
                    parentLayout.setOrientation(LinearLayout.VERTICAL);

                    RelativeLayout upLayout = new RelativeLayout(getApplicationContext());
                    upLayout.setPadding(0, 10, 20, 0);
                    int relative_MATCH_PARENT = RelativeLayout.LayoutParams.MATCH_PARENT;
                    int relative_WRAP_CONTENT = RelativeLayout.LayoutParams.WRAP_CONTENT;
                    RelativeLayout.LayoutParams upLayoutParams = new RelativeLayout.LayoutParams(relative_MATCH_PARENT, relative_WRAP_CONTENT);
                    upLayout.setLayoutParams(upLayoutParams);
                    TextView index = new TextView(getApplicationContext());
                    RelativeLayout.LayoutParams tvLayoutParams = new android.widget.RelativeLayout.LayoutParams(
                            relative_WRAP_CONTENT, relative_WRAP_CONTENT);
                    tvLayoutParams
                            .addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    index.setLayoutParams(tvLayoutParams);
                    index.setText("index/index");
                    index.setTag("index");
                    upLayout.addView(index);
                    parentLayout.addView(upLayout);

                    LinearLayout photoLayout = new LinearLayout(getApplicationContext());
                    LinearLayout.LayoutParams photoLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    photoLayoutParams.setMargins(5, 20, 5, 0);
                    photoLayout.setLayoutParams(photoLayoutParams);
                    photoLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                    photoLayout.setOrientation(LinearLayout.VERTICAL);
                    photoLayout.setPadding(0, 10, 0, 10);
                    ImageView photoPreView = new ImageView(getApplicationContext());
                    photoPreView.setId(randomID);
//                    int sixtyPercentOfWidth =displayManager.getWidth(60);
                    photoPreView.setLayoutParams(new LinearLayout.LayoutParams(6, 6));
                    photoLayout.addView(photoPreView);

                    Button button = new Button(getApplicationContext());
//                    int sevenPersentHeight = displayManager.getHeigth(7);
                    LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 7);
                    buttonLayoutParams.setMargins(30, 20, 30, 10);
                    button.setLayoutParams(buttonLayoutParams);
                    button.setText("Select Photo");
                    button.setBackgroundResource(R.drawable.border);
                    click.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectImage(fieldName, fieldType, "", viewId);
                        }
                    });
                    photoLayout.addView(button);
                    scroll.addView(photoLayout);

                    parentLayout.addView(scroll);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getMessage().equals(getResources().getString(R.string.api_error_offline))) {
                    Toast bread = Toast.makeText(getApplicationContext(), R.string.api_error_offline_message, Toast.LENGTH_SHORT);
                    bread.show();
                }
                Log.d("API_Profile", error.toString());

            }
        });
    }

    public void submitted(){
        btnOk.setText(getResources().getString(R.string.button_label_loading));
        btnOk.setEnabled(false);
    }

    public void toBeSubmitted(){
        btnOk.setEnabled(true);
        btnOk.setText(getResources().getString(R.string.button_label_done));
    }

    String imagePath="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppValues.REQUEST_CAMERA
                && resultCode == RESULT_OK) {
            // Bundle bundle = data.getExtras();
            // bundle.getString("photo_path");
            String field_name = AppValues.FIELD_NAME_TMP;
            String field_help = AppValues.FIELD_HELP_TMP;
            String field_type = AppValues.FIELD_TYPE_TMP;
            int view_id = Integer.parseInt(AppValues.VIEW_ID_TMP);
             imagePath = AppValues.IMAGE_PATH_TMP;
            Log.i("Image Path",AppValues.IMAGE_PATH_TMP);
        }
        else if (requestCode == AppValues.SELECT_FILE
                && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            String tempPath = getPath(selectedImageUri, EditProfileActivity.this);
            String field_name = AppValues.FIELD_NAME_TMP;
            String field_help = AppValues.FIELD_HELP_TMP;
            String field_type = AppValues.FIELD_TYPE_TMP;
            int view_id = Integer.parseInt(AppValues.VIEW_ID_TMP);
            imagePath = tempPath;
            Log.i("image Path",imagePath);
        }
        if(imagePath!=""){
            Picasso.with(this).load("file://"+imagePath)
                    .error(android.R.drawable.stat_notify_error)
                    .placeholder(android.R.drawable.stat_notify_sync)
                    .transform(new CircleTransform()).into(iVProfilePr);

            iVProfilePr.setTag(imagePath);
        }

    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        CursorLoader loader = new CursorLoader(activity, uri, projection, null,
                null, null);
        // following is to test cuz the upper method is deprecated
        // Cursor cursor2 = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static int randInt(int min,int max){
        Random rand = new Random();
        int randomNum =rand.nextInt((max-min)+1)+min;
        return randomNum;
    }

    private void selectImage(final String field_name, final String field_type,
                             final String field_help, final String view_id) {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AppValues.setFIELD_NAME_TMP(field_name);
        AppValues.setFIELD_TYPE_TMP(field_type);
        AppValues.setFIELD_HELP_TMP(field_help);
        AppValues.setVIEW_ID_TMP(view_id);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File myDir = new File(AppValues.getXAVEY_DIRECTORY(),
                            "/Photos");
                    myDir.mkdirs();
                    String photoName = "w_photo" + System.currentTimeMillis()
                            + ".jpg";
                    File f = new File(myDir, photoName);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent,AppValues.getREQUEST_CAMERA());
                    AppValues.setIMAGE_PATH_TMP ( myDir + "/" + photoName);
                    AppValues.setPHOTO_NAME_TMP (photoName);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent,AppValues.getSELECT_FILE());
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
}
