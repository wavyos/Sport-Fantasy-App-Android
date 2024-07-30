package com.example.sportsfantasy.Fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportsfantasy.Adapter.ArticalAdapter
import com.example.sportsfantasy.Model.ArticalData
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.FragmentNewsBinding


class NewsFragment : Fragment()
{
    lateinit var binding:FragmentNewsBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.inflate(layoutInflater)

        setEvents()
        setRecyclerViewData()

        return binding.root
    }

    private fun setEvents() {
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setRecyclerViewData() {
        val tempArray = ArrayList<ArticalData>()
        tempArray.add(ArticalData("Scoot Henderson","波特兰开拓者队（在联赛中入选了54.4%的球队）","Scoot Henderson，波特兰开拓者队（在联赛中入选了54.4%的球队）：赛季初步进展缓慢，加上一场持续存在的伤病，使得亨德森的幻想价值下降，尽管最近的复出以及上周的一场大型两双表明他即将迎来一波上升。这支球队可能会交易一些资深球员，尤其是马尔科姆·布罗格登，这将立即使亨德森扮演重要角色。",
            ContextCompat.getDrawable(requireContext(), R.drawable.p_one)!!
        ))
        tempArray.add(ArticalData("Dante Exum","达拉斯小牛队（16.6%入选）","Dante Exum，达拉斯小牛队（16.6%入选）：\"复苏\"可能并不是正确的词语，因为在艾克萨姆早期的NBA征程中，他从未达到过这样的水平。但自从重返联盟并由于凯里·欧文的伤病而接任首发任务以来，他在两端的贡献者方面表现出色。",ContextCompat.getDrawable(requireContext(), R.drawable.p_two)!!))
        tempArray.add(ArticalData("Malik Monk","萨克拉门托国王队（39.2%入选）","Malik Monk，萨克拉门托国王队（39.2%入选）：在引起幻想经理关注方面取得了一些进展，蒙克是罕见的替补得分手，具有真正的组织进攻潜力。一个替补领袖能够保持高助攻率是罕见的，但蒙克是我们应该珍视的这个例外。",ContextCompat.getDrawable(requireContext(), R.drawable.p_three)!!))
        tempArray.add(ArticalData("Caleb Martin","迈阿密热火队（12.6%入选）","Caleb Martin，迈阿密热火队（12.6%入选）：一旦泰勒·希罗回归，马丁似乎不会失去太多出场时间或机会。我们在去年夏天看到马丁在球队的总决赛中表现出色，而他现在又回到了成为球场两端连接者的状态。",ContextCompat.getDrawable(requireContext(), R.drawable.p_four)!!))
        tempArray.add(ArticalData("Collin Sexton","犹他爵士队（40.2%入选）","Collin Sexton，犹他爵士队（40.2%入选）：由于犹他后场一些球员受伤，塞克斯顿在最近几场比赛中看到了更多的上场时间和机会。得分产出的增加表明他有能力保持一个有意义的角色。",ContextCompat.getDrawable(requireContext(), R.drawable.p_five)!!))
        tempArray.add(ArticalData("Brandin Podziemski","金州勇士队（9.5%入选）","Brandin Podziemski，金州勇士队（9.5%入选）：鉴于德雷蒙德·格林的缺席，这位新秀迅速成为首发球员。凭借出色的篮板技术和一些潜力，\"Podz\"在深度联赛中值得占据一个名额。",ContextCompat.getDrawable(requireContext(), R.drawable.p_six)!!))
        tempArray.add(ArticalData("Trey Murphy III","新奥尔良鹈鹕队（23.4%入选）","Trey Murphy III，新奥尔良鹈鹕队（23.4%入选）：球队正在让墨菲重新扮演一个重要角色。无论他是首发还是替补，他的三分和防守技能都很出色。毕竟，他在季前手术康复期间已经以40%的三分命中率场均贡献16分。",ContextCompat.getDrawable(requireContext(), R.drawable.p_seven)!!))
        tempArray.add(ArticalData("戈登·海沃德","夏洛特黄蜂队（55.6%入选）","戈登·海沃德，夏洛特黄蜂队（55.6%入选）：最近因疾病而休战，自从拉梅洛·鲍尔受伤以来，海沃德在组织进攻和产出方面有了显著提升。耐久性始终是一个问题，但当海沃德处于活跃状态时，他的价值就显而易见。",ContextCompat.getDrawable(requireContext(), R.drawable.p_eight)!!))
        tempArray.add(ArticalData("Tari Eason","休斯顿火箭队（14.4%入选）","Tari Eason，休斯顿火箭队（14.4%入选）：由于不竭的能量和真正的两端技能，伊森赢得了更多的上场时间，他是罕见的能在篮板方面帮助你的侧翼球员。",ContextCompat.getDrawable(requireContext(), R.drawable.p_nine)!!))
        tempArray.add(ArticalData("波扬·博格达诺维奇","底特律活塞队（50.5%入选）","波扬·博格达诺维奇，底特律活塞队（50.5%入选）：在得分和投篮之外，你还能得到什么呢？实际上并没有太多。但专业球员仍然有价值，尤其是那些能够高效地保持每场20分的球员，正如博吉所能做到的。",ContextCompat.getDrawable(requireContext(), R.drawable.p_ten)!!))
        tempArray.add(ArticalData("帕特里克·威廉姆斯","芝加哥公牛队（7.8%入选）","帕特里克·威廉姆斯，芝加哥公牛队（7.8%入选）：一个悄悄崛起的球员，芝加哥看到科比·怀特和威廉姆斯都成为了已经实现的首发级别的贡献者。威廉姆斯的底线比高使用率的怀特要低，但即使投篮不准，他的有趣的防守数据仍然保持稳定。",ContextCompat.getDrawable(requireContext(), R.drawable.p_eleven)!!))
        tempArray.add(ArticalData("戴瑞克·莱夫利二世","达拉斯小牛队（29.1%入选）","戴瑞克·莱夫利二世，达拉斯小牛队（29.1%入选）：最近踝关节受伤使他在本周一开始无法上场，但鉴于他是达拉斯阵容中唯一真正的篮下得分者，莱夫利仍然是一个顶级的选择，具有出色的生产潜力。",ContextCompat.getDrawable(requireContext(), R.drawable.p_twelve)!!))
        tempArray.add(ArticalData("以赛亚·哈滕斯坦","纽约尼克斯队（8.6%入选）","以赛亚·哈滕斯坦，纽约尼克斯队（8.6%入选）：米切尔·罗宾逊的伤势将哈滕斯坦推向了未来几周内的一个重要角色。到目前为止，他以出色的篮板和盖帽率作出了回应。",ContextCompat.getDrawable(requireContext(), R.drawable.p_thirteen)!!))


        binding.rvArticals.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = ArticalAdapter(requireContext(), tempArray)
        binding.rvArticals.adapter = adapter
    }

}