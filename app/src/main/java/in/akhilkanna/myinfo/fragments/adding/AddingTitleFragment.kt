package `in`.akhilkanna.myinfo.fragments.adding

import `in`.akhilkanna.myinfo.R
import `in`.akhilkanna.myinfo.dataStructures.Title
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_adding_title.*
import kotlinx.android.synthetic.main.fragment_adding_title.view.*
import android.app.Activity



/**
 * A placeholder fragment containing a simple view.
 */
class AddingTitleFragment : Fragment() {

    var editMode : Boolean = false
    var editingTitle : Title? = null
    var titleCreatedCallBack: TitleCreationListener? = null

    interface TitleCreationListener {
        fun onTitleCreated(title: Title)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_adding_title, container, false)

        val editingTitleId = arguments.getInt("editingTitle", -1)
        if (editingTitleId != -1) {
            editingTitle = Title.get(context, editingTitleId)
        }

        save_title_fab.setOnClickListener {
            if (!rootView.name_text.text.isEmpty()){

                if (editMode && editingTitle != null) {
                    editingTitle?.title = rootView.name_text.text.toString()
                    editingTitle?.isProtected = rootView.is_protected.isChecked
                    if (!editingTitle?.commit(context)!!){
                        Snackbar.make(rootView, "Failed", Snackbar.LENGTH_LONG).show()
                    } else {
                        Snackbar.make(rootView, "Successfully updated.", Snackbar.LENGTH_LONG).show()
                        // TODO success
                    }
                }

                val title = Title.create(context, rootView.name_text.text.toString(), rootView.is_protected.isChecked)
                val status = if (title != null) "Title Added" else "Failed"
                Snackbar.make(rootView , status, Snackbar.LENGTH_LONG).show()

                if (title != null && titleCreatedCallBack != null) {
                    titleCreatedCallBack?.onTitleCreated(title)
                }
            }
            else {
                Snackbar.make(rootView , "Please Enter the title.", Snackbar.LENGTH_LONG).show()
            }
        }

        return rootView
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            titleCreatedCallBack = activity as TitleCreationListener?
        } catch (e: ClassCastException) {
            throw ClassCastException(activity!!.toString() + " must implement TitleCreationListener")
        }

    }


}