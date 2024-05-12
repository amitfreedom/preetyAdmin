package com.adminlive.preetyadminpanel.agency.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adminlive.preetyadminpanel.ApplicationClass;
import com.adminlive.preetyadminpanel.Constant;
import com.adminlive.preetyadminpanel.R;
import com.adminlive.preetyadminpanel.agency.ui.home.adapter.AgencyHostUser;
import com.adminlive.preetyadminpanel.databinding.FragmentAgencyHomeBinding;
import com.adminlive.preetyadminpanel.global.ExcelUtils;
import com.adminlive.preetyadminpanel.ui.host.modal.HostModal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AgencyHomeFragment extends Fragment {
    private static final String TAG = "AgencyHomeFragment";
    private FragmentAgencyHomeBinding binding;

    HostModal hostModal;
    private String status="";

    private ArrayList<HostModal> hostModalArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAgencyHomeBinding.inflate(inflater, container, false);
        hostModalArrayList= new ArrayList<>();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fetchAgencyDetails("0098");

//        createExcelFile();
    }

    private void createExcelFile() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Name", "Email", "Phone","age"});
        data.add(new String[]{"John Doe", "john@example.com", "1234567890","45"});
        data.add(new String[]{"Jane Smith", "jane@example.com", "9876543210","58"});

        // Create Excel file and get the path of the created zip file
//        String zipFilePath = ExcelUtils.createExcelFile(requireContext(), "salary_sheet.xls", data);
        ExcelUtils.createHostExcelFile(requireContext(), "salary_sheet.xls", hostModalArrayList, new ExcelUtils.ExcelCreationListener() {
            @Override
            public void onExcelFileCreated(String zipFilePath) {
// Handle the notification here, e.g., display a toast or update UI
                if (!zipFilePath.isEmpty()) {
                    // Share the zip file via email
                    Toast.makeText(requireActivity(), "Excel file created successfully", Toast.LENGTH_SHORT).show();
                    ExcelUtils.shareZipFileViaEmail(requireActivity(), zipFilePath);
                } else {
                    Toast.makeText(requireActivity(), "Error creating Excel file", Toast.LENGTH_SHORT).show();
                }
            }
        });



//        String success = ExcelUtils.createExcelFile(requireActivity(), "my_excel_file.xls", data);
//        if (success) {
//            Toast.makeText(requireActivity(), "Excel file created successfully", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(requireActivity(), "Failed to create Excel file", Toast.LENGTH_SHORT).show();
//        }

    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//        fetchAgencyDetails("0098");
    }

    public void fetchAgencyDetails(String agencyCode) {
        FirebaseFirestore.getInstance().collection(Constant.HOST_AGENCY).document(agencyCode).collection("host")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            hostModalArrayList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Convert the document to your host model class
                                hostModal = document.toObject(HostModal.class);
                                hostModalArrayList.add(hostModal);
                            }
                            ApplicationClass.getSingleton().setTotalUserInsideHost(String.valueOf(hostModalArrayList.size()));
                            AgencyHostUser agencyHostUser = new AgencyHostUser(requireActivity(), hostModalArrayList, new AgencyHostUser.Select() {
                                @Override
                                public void pressItem(HostModal hostModal) {
                                    createExcelFile();

//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("userId",hostModal.getUserId());
//                                    Navigation.findNavController(requireView()).navigate(R.id.action_navigation_home_to_agencyHostDetailsFragment,bundle);

                                }
                            });
                            binding.recyclerAgencyList.setAdapter(agencyHostUser);
                            agencyHostUser.notifyDataSetChanged();

                            if (hostModalArrayList.size()==0){
                                binding.recyclerAgencyList.setVisibility(View.GONE);
                                binding.notFound.setVisibility(View.VISIBLE);
                            }else {
                                binding.recyclerAgencyList.setVisibility(View.VISIBLE);
                                binding.notFound.setVisibility(View.INVISIBLE);
                            }

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding=null;
    }
}