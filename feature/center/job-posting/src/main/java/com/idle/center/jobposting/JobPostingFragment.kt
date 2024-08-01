package com.idle.center.jobposting

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.idle.compose.addFocusCleaner
import com.idle.compose.base.BaseComposeFragment
import com.idle.designsystem.compose.component.CareButtonStrokeSmall
import com.idle.designsystem.compose.component.CareProgressBar
import com.idle.designsystem.compose.component.CareStateAnimator
import com.idle.designsystem.compose.component.CareSubtitleTopAppBar
import com.idle.domain.model.auth.Gender
import com.idle.domain.model.job.ApplyMethod
import com.idle.domain.model.job.DayOfWeek
import com.idle.domain.model.job.LifeAssistance
import com.idle.domain.model.job.MentalStatus
import com.idle.domain.model.job.PayType
import com.idle.post.code.PostCodeFragment
import com.idle.signup.center.step.AdditionalInfoScreen
import com.idle.signup.center.step.AddressScreen
import com.idle.signup.center.step.CustomerInformationScreen
import com.idle.signup.center.step.CustomerRequirementScreen
import com.idle.signup.center.step.SummaryScreen
import com.idle.signup.center.step.TimePaymentScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class JobPostingFragment : BaseComposeFragment() {

    override val fragmentViewModel: JobPostingViewModel by viewModels()

    private val postCodeDialog: PostCodeFragment? by lazy {
        PostCodeFragment().apply {
            onDismissCallback = {
                findNavController().currentBackStackEntry?.savedStateHandle?.let {
                    val roadNameAddress = it.get<String>("roadNameAddress")
                    val lotNumberAddress = it.get<String>("lotNumberAddress")

                    fragmentViewModel.setRoadNameAddress(roadNameAddress ?: "")
                    fragmentViewModel.setLotNumberAddress(lotNumberAddress ?: "")
                }
            }
        }
    }

    @Composable
    override fun ComposeLayout() {
        fragmentViewModel.apply {
            val jobPostingStep by registerProcess.collectAsStateWithLifecycle()
            val weekDays by weekDays.collectAsStateWithLifecycle()
            val payType by payType.collectAsStateWithLifecycle()
            val payAmount by payAmount.collectAsStateWithLifecycle()
            val roadNameAddress by roadNameAddress.collectAsStateWithLifecycle()
            val detailAddress by detailAddress.collectAsStateWithLifecycle()
            val gender by gender.collectAsStateWithLifecycle()
            val birthYear by birthYear.collectAsStateWithLifecycle()
            val weight by weight.collectAsStateWithLifecycle()
            val careLevel by careLevel.collectAsStateWithLifecycle()
            val mentalStatus by mentalStatus.collectAsStateWithLifecycle()
            val disease by disease.collectAsStateWithLifecycle()
            val isMealAssistance by isMealAssistance.collectAsStateWithLifecycle()
            val isBowelAssistance by isBowelAssistance.collectAsStateWithLifecycle()
            val isWalkingAssistance by isWalkingAssistance.collectAsStateWithLifecycle()
            val lifeAssistance by lifeAssistance.collectAsStateWithLifecycle()
            val speciality by speciality.collectAsStateWithLifecycle()
            val isExperiencePreferred by isExperiencePreferred.collectAsStateWithLifecycle()
            val applyMethod by applyMethod.collectAsStateWithLifecycle()
            val applyDeadlineChipState by applyDeadlineChipState.collectAsStateWithLifecycle()
            val applyDeadline by applyDeadline.collectAsStateWithLifecycle()

            JobPostingScreen(
                weekDays = weekDays,
                payType = payType,
                payAmount = payAmount,
                roadNameAddress = roadNameAddress,
                detailAddress = detailAddress,
                gender = gender,
                birthYear = birthYear,
                weight = weight,
                careLevel = careLevel,
                mentalStatus = mentalStatus,
                disease = disease,
                isMealAssistance = isMealAssistance,
                isBowelAssistance = isBowelAssistance,
                isWalkingAssistance = isWalkingAssistance,
                lifeAssistance = lifeAssistance,
                speciality = speciality,
                isExperiencePreferred = isExperiencePreferred,
                applyMethod = applyMethod,
                applyDeadlineChipState = applyDeadlineChipState,
                applyDeadline = applyDeadline,
                jobPostingStep = jobPostingStep,
                setWeekDays = ::setWeekDays,
                onPayTypeChanged = ::setPayType,
                onPayAmountChanged = ::setPayAmount,
                onDetailAddressChanged = ::setDetailAddress,
                showPostCodeDialog = {
                    if (!(postCodeDialog?.isAdded == true || postCodeDialog?.isVisible == true)) {
                        postCodeDialog?.show(parentFragmentManager, "PostCodeFragment")
                    }
                },
                onGenderChanged = ::setGender,
                onWeightChanged = ::setWeight,
                onMentalStatusChanged = ::setMentalStatus,
                onBirthYearChanged = ::setBirthYear,
                onCareLevelChanged = ::setCareLevel,
                onDiseaseChanged = ::setDisease,
                onMealAssistanceChanged = ::setMealAssistance,
                onBowelAssistanceChanged = ::setBowelAssistance,
                onWalkingAssistanceChanged = ::setWalkingAssistance,
                onLifeAssistanceChanged = ::setLifeAssistance,
                onSpecialityChanged = ::setSpeciality,
                onExperiencePreferredChanged = ::setExperiencePreferred,
                onApplyMethodChanged = ::setApplyMethod,
                onApplyDeadlineChipStateChanged = ::setApplyDeadlineChipState,
                onApplyDeadlineChanged = ::setApplyDeadline,
                setJobPostingStep = ::setJobPostingStep,
            )
        }
    }
}

@Composable
internal fun JobPostingScreen(
    weekDays: Set<DayOfWeek>,
    payType: PayType?,
    payAmount: String,
    roadNameAddress: String,
    detailAddress: String,
    gender: Gender,
    birthYear: String,
    weight: String,
    careLevel: String,
    mentalStatus: MentalStatus,
    disease: String,
    isMealAssistance: Boolean?,
    isBowelAssistance: Boolean?,
    isWalkingAssistance: Boolean?,
    lifeAssistance: Set<LifeAssistance>,
    speciality: String,
    isExperiencePreferred: Boolean?,
    applyMethod: Set<ApplyMethod>,
    applyDeadlineChipState: ApplyDeadlineChipState?,
    applyDeadline: String,
    jobPostingStep: JobPostingStep,
    setWeekDays: (DayOfWeek) -> Unit,
    onPayTypeChanged: (PayType) -> Unit,
    onPayAmountChanged: (String) -> Unit,
    onDetailAddressChanged: (String) -> Unit,
    showPostCodeDialog: () -> Unit,
    onGenderChanged: (Gender) -> Unit,
    onBirthYearChanged: (String) -> Unit,
    onWeightChanged: (String) -> Unit,
    onCareLevelChanged: (String) -> Unit,
    onMentalStatusChanged: (MentalStatus) -> Unit,
    onDiseaseChanged: (String) -> Unit,
    onMealAssistanceChanged: (Boolean) -> Unit,
    onBowelAssistanceChanged: (Boolean) -> Unit,
    onWalkingAssistanceChanged: (Boolean) -> Unit,
    onLifeAssistanceChanged: (LifeAssistance) -> Unit,
    onSpecialityChanged: (String) -> Unit,
    onExperiencePreferredChanged: (Boolean) -> Unit,
    onApplyMethodChanged: (ApplyMethod) -> Unit,
    onApplyDeadlineChanged: (String) -> Unit,
    onApplyDeadlineChipStateChanged: (ApplyDeadlineChipState) -> Unit,
    setJobPostingStep: (JobPostingStep) -> Unit,
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            CareSubtitleTopAppBar(
                title = if (jobPostingStep != JobPostingStep.SUMMARY) "공고 등록" else "",
                onNavigationClick = { onBackPressedDispatcher?.onBackPressed() },
                leftComponent = {
                    if (jobPostingStep == JobPostingStep.SUMMARY) {
                        CareButtonStrokeSmall(
                            text = "공고 수정하기",
                            onClick = { },
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 12.dp, top = 48.dp, end = 20.dp),
            )
        },
        modifier = Modifier.addFocusCleaner(focusManager),
    ) { paddingValue ->
        CareStateAnimator(
            targetState = jobPostingStep,
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
                .padding(paddingValue)
        ) { step ->
            if (step == JobPostingStep.SUMMARY) {
                SummaryScreen(
                    weekDays = weekDays,
                    payType = payType,
                    payAmount = payAmount,
                    roadNameAddress = roadNameAddress,
                    gender = gender,
                    birthYear = birthYear,
                    weight = weight,
                    careLevel = careLevel,
                    mentalStatus = mentalStatus,
                    disease = disease,
                    isMealAssistance = isMealAssistance,
                    isBowelAssistance = isBowelAssistance,
                    isWalkingAssistance = isWalkingAssistance,
                    lifeAssistance = lifeAssistance,
                    speciality = speciality,
                    isExperiencePreferred = isExperiencePreferred,
                    applyMethod = applyMethod,
                    applyDeadlineChipState = applyDeadlineChipState,
                    applyDeadline = applyDeadline,
                    setWeekDays = setWeekDays,
                    onPayTypeChanged = onPayTypeChanged,
                    onPayAmountChanged = onPayAmountChanged,
                    onDetailAddressChanged = onDetailAddressChanged,
                    showPostCodeDialog = showPostCodeDialog,
                    onGenderChanged = onGenderChanged,
                    onBirthYearChanged = onBirthYearChanged,
                    onWeightChanged = onWeightChanged,
                    onCareLevelChanged = onCareLevelChanged,
                    onMentalStatusChanged = onMentalStatusChanged,
                    onDiseaseChanged = onDiseaseChanged,
                    onMealAssistanceChanged = onMealAssistanceChanged,
                    onBowelAssistanceChanged = onBowelAssistanceChanged,
                    onWalkingAssistanceChanged = onWalkingAssistanceChanged,
                    onLifeAssistanceChanged = onLifeAssistanceChanged,
                    onSpecialityChanged = onSpecialityChanged,
                    onExperiencePreferredChanged = onExperiencePreferredChanged,
                    onApplyMethodChanged = onApplyMethodChanged,
                    onApplyDeadlineChanged = onApplyDeadlineChanged,
                    onApplyDeadlineChipStateChanged = onApplyDeadlineChipStateChanged,
                    setJobPostingStep = setJobPostingStep
                )
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 8.dp),
                ) {
                    CareProgressBar(
                        currentStep = jobPostingStep.step,
                        totalSteps = JobPostingStep.entries.size - 1,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    CareStateAnimator(
                        targetState = jobPostingStep,
                        label = "센터 정보 입력을 관리하는 애니메이션",
                    ) { jobPostingStep ->
                        when (jobPostingStep) {
                            JobPostingStep.TIME_PAYMENT -> TimePaymentScreen(
                                weekDays = weekDays,
                                payType = payType,
                                payAmount = payAmount,
                                setWeekDays = setWeekDays,
                                setPayType = onPayTypeChanged,
                                setPayAmount = onPayAmountChanged,
                                setJobPostingStep = setJobPostingStep,
                            )

                            JobPostingStep.ADDRESS -> AddressScreen(
                                roadNameAddress = roadNameAddress,
                                detailAddress = detailAddress,
                                onDetailAddressChanged = onDetailAddressChanged,
                                showPostCodeDialog = showPostCodeDialog,
                                setJobPostingStep = setJobPostingStep,
                            )

                            JobPostingStep.CUSTOMER_INFORMATION -> CustomerInformationScreen(
                                gender = gender,
                                birthYear = birthYear,
                                weight = weight,
                                careLevel = careLevel,
                                mentalStatus = mentalStatus,
                                disease = disease,
                                onGenderChanged = onGenderChanged,
                                onBirthYearChanged = onBirthYearChanged,
                                onWeightChanged = onWeightChanged,
                                onCareLevelChanged = onCareLevelChanged,
                                onMentalStatusChanged = onMentalStatusChanged,
                                onDiseaseChanged = onDiseaseChanged,
                                setJobPostingStep = setJobPostingStep,
                            )

                            JobPostingStep.CUSTOMER_REQUIREMENT -> CustomerRequirementScreen(
                                isMealAssistance = isMealAssistance,
                                isBowelAssistance = isBowelAssistance,
                                isWalkingAssistance = isWalkingAssistance,
                                lifeAssistance = lifeAssistance,
                                speciality = speciality,
                                setMealAssistance = onMealAssistanceChanged,
                                setBowelAssistance = onBowelAssistanceChanged,
                                setWalkingAssistance = onWalkingAssistanceChanged,
                                setLifeAssistance = onLifeAssistanceChanged,
                                setSpeciality = onSpecialityChanged,
                                setJobPostingStep = setJobPostingStep,
                            )

                            JobPostingStep.ADDITIONAL_INFO -> AdditionalInfoScreen(
                                isExperiencePreferred = isExperiencePreferred,
                                applyMethod = applyMethod,
                                applyDeadlineChipState = applyDeadlineChipState,
                                applyDeadline = applyDeadline,
                                onExperiencePreferredChanged = onExperiencePreferredChanged,
                                onApplyMethodChanged = onApplyMethodChanged,
                                onApplyDeadlineChipStateChanged = onApplyDeadlineChipStateChanged,
                                onApplyDeadlineChanged = onApplyDeadlineChanged,
                                setJobPostingStep = setJobPostingStep,
                            )

                            JobPostingStep.SUMMARY -> Box(modifier = Modifier.fillMaxSize())
                        }
                    }
                }
            }
        }
    }
}