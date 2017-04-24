package id.co.ruma.guestbook

import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmRecyclerViewAdapter
import org.androidannotations.annotations.*
import javax.inject.Inject

@EActivity(R.layout.activity_home)
open class MainActivity : AppCompatActivity() {
    @ViewById(R.id.toolbar)
    @JvmField var toolbar: Toolbar? = null
    @ViewById(R.id.guest_list)
    @JvmField var mGuestListView: RecyclerView? = null

    @Inject
    @JvmField var realm:Realm? = null

    @AfterViews
    fun onViewInit() {
        setSupportActionBar(toolbar)
        GuestBookApp.guestBookComponent.inject(this)
        mGuestListView?.layoutManager = LinearLayoutManager(this)
        mGuestListView?.adapter = GuestAdapter(realm?.where(Guest::class.java)?.findAllAsync())
    }

    @Click(R.id.fab)
    fun onAddClicked() {
        showInputDialog()
    }

    private fun showInputDialog() {
        val dialogView = this.layoutInflater.inflate(R.layout.dialog_input, null)
        val etName = dialogView.findViewById(R.id.et_name) as EditText
        val etDivision = dialogView.findViewById(R.id.et_division) as EditText
        AlertDialog.Builder(this).setView(dialogView).setTitle("Add Guest Data")
                .setPositiveButton("Add") { dialog, whichButton ->
                    val name = etName.text.toString()
                    val division = etDivision.text.toString()
                    if (!name.isBlank() && !division.isBlank()) {
                        realm?.executeTransaction { realm?.insertOrUpdate(Guest(name, division)) }
                    }
                }
                .setNegativeButton("Cancel") { dialog, whichButton -> }
                .create().show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_reset -> {
                realm?.executeTransaction { realm?.delete(Guest::class.java) }; true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    class GuestAdapter(data: OrderedRealmCollection<Guest>?, autoUpdate: Boolean) : RealmRecyclerViewAdapter<Guest, GuestViewHolder>(data, autoUpdate) {
        constructor(data: OrderedRealmCollection<Guest>?) : this(data, true)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestViewHolder {
            val context = parent.context
            val guestViewHolder = GuestViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.item_guest, parent, false))
            return guestViewHolder
        }

        override fun onBindViewHolder(holder: GuestViewHolder, position: Int) {
            val guest = data!![position]
            holder.mNameView.text = guest.name
            holder.mDivisionView.text = guest.division
        }
    }

    class GuestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var mNameView: TextView = view.findViewById(R.id.name) as TextView
        var mDivisionView: TextView = view.findViewById(R.id.division) as TextView
    }
}

open class Guest() : RealmObject() {
    var name: String? = null
    var division: String? = null

    constructor(name: String, division: String) : this() {
        this.name = name
        this.division = division
    }
}
