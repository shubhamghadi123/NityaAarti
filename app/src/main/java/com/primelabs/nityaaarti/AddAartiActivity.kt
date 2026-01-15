package com.primelabs.nityaaarti

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AddAartiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_aarti)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        val aartiList = ArrayList<AartiListItem>()

        // -- श्री गणपतीची आरती --
        aartiList.add(AartiListItem(AartiListItem.TYPE_HEADER, "श्री गणपतीची आरती"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "सुखकर्ता दुःखहर्ता"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "शेंदुर लाल चढ़ायो"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "नाना परिमळ"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "उंदरावर बैसोनी दुड्डा येसी"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "तू सुखकर्ता तू दुःखहर्ता विघ्न विनाशक मोरया"))

        // -- श्री शंकराची आरती --
        aartiList.add(AartiListItem(AartiListItem.TYPE_HEADER, "श्री शंकराची आरती"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "लवथवती विक्राळा"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "सदया सगुणा शंभो अजिनांबरधारी"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "पडलें गोहत्येचें पातक गौतमऋषिच्या शिरी हो"))

        // -- देवीची आरती --
        aartiList.add(AartiListItem(AartiListItem.TYPE_HEADER, "देवीची आरती"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "दुर्गे दुर्घट भारी"))

        // -- श्री हनुमंताची आरती --
        aartiList.add(AartiListItem(AartiListItem.TYPE_HEADER, "श्री हनुमंताची आरती"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "सत्राणे उड्डाणे हुंकार वदनी"))

        // -- श्री गुरुदत्ता आरती --
        aartiList.add(AartiListItem(AartiListItem.TYPE_HEADER, "श्री गुरुदत्ता आरती"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "त्रिगुणात्मक त्रैमूर्ती"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "धन्य धन्य हो प्रदक्षिणा"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "आरती गुरुदत्ता स्वामि सत्येंद्रनाथा"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "हरिदासस्वामी महाराजांची आरती"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "श्रीमत् नृसिंह सरस्वतीस्वामी महाराजांची आरती"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "भूपाळी श्रीदत्तात्रेयांची आरती"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "कांकडे आरती"))

        // -- श्रीपाद श्रीवल्लभांची आरती --
        aartiList.add(AartiListItem(AartiListItem.TYPE_HEADER, "श्रीपाद श्रीवल्लभांची आरती"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "श्रीपाद श्रीवल्लभ जय जय करुणाकर मूर्ती"))

        // -- श्री विठ्ठलाची आरती --
        aartiList.add(AartiListItem(AartiListItem.TYPE_HEADER, "श्री विठ्ठलाची आरती"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "युगे अठ्ठावीस विटेवरी उभा"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "येई हो विठ्ठले माझे माउली ये"))

        // -- साईबाबांची आरती --
        aartiList.add(AartiListItem(AartiListItem.TYPE_HEADER, "श्री साईबाबांची आरती"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "आरती साईबाबा"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "श्री सद्गुरू साईबाबांचे भक्तांना अभय"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "घेउनियां पंचारती करूं बाबांसी आरती"))

        // -- समर्थ श्रीचंद्रशेखर स्वामी महाराजांची आरती --
        aartiList.add(AartiListItem(AartiListItem.TYPE_HEADER, "समर्थ श्रीचंद्रशेखर स्वामी महाराजांची आरती"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "आरती सद्गुरुनाथा चंद्रशेखर समर्था"))

        // -- श्रीसत्येंद्रनाथ स्वामी महाराजांची आरती --
        aartiList.add(AartiListItem(AartiListItem.TYPE_HEADER, "श्रीसत्येंद्रनाथ स्वामी महाराजांची आरती"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "ओवाळू आरती सद्गुरु राया"))

        // -- ज्ञानराजा आरती --
        aartiList.add(AartiListItem(AartiListItem.TYPE_HEADER, "ज्ञानराजा आरती"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "आरती ज्ञानराजा"))

        // -- सूर्याची आरती --
        aartiList.add(AartiListItem(AartiListItem.TYPE_HEADER, "सूर्याची आरती"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "जय जय जगतम हरणा"))

        // -- दशावतारांची आरती --
        aartiList.add(AartiListItem(AartiListItem.TYPE_HEADER, "दशावतारांची आरती"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "आरती सप्रेम जय जय विठ्ठल परब्रह्म"))

        // -- प्रार्थना --
        aartiList.add(AartiListItem(AartiListItem.TYPE_HEADER, "प्रार्थना"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "घालीन लोटांगण"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "मंत्रपुष्पांजली"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "निरोप गीत"))
        aartiList.add(AartiListItem(AartiListItem.TYPE_AARTI, "पसायदान"))

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerAllAartis)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val alreadySaved = AartiStorage.getSavedAartis(this).toMutableList()

        val adapter = AartiAdapter(
            items = aartiList,      // Your full list
            savedAartis = alreadySaved, // NEW: The saved list
            onAddClick = { aartiName ->
                AartiStorage.addAarti(this, aartiName)
                Toast.makeText(this, "$aartiName added!", Toast.LENGTH_SHORT).show()
            },
            onItemClick = { aartiName ->
                val intent = Intent(this, ReadAartiActivity::class.java)
                intent.putExtra("AARTI_NAME", aartiName)
                intent.putExtra("HIDE_NAV", true)
                startActivity(intent)
            }
        )

        recyclerView.adapter = adapter
    }
}