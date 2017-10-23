package com.innohub.innosorter.management;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.innohub.innosorter.entity.Administrator;
import com.innohub.innosorter.entity.Cluster;
import com.innohub.innosorter.entity.Developer;
import com.innohub.innosorter.entity.Post;
import com.innohub.innosorter.repo.IssueRepositoryService;
import com.innohub.innosorter.util.ApplicationConstants;

public class IssueManagerTest {

    @InjectMocks
    private IssueManager issueManager;

    @Mock
    private IssueRepositoryService mockIssueRepositoryService;

    @Before
    public void setUp(){
        initMocks(this);
    }

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Test
    public void shouldAllowAdminUserToAddForumPostToCluster(){
        //Given
        Cluster clusterOne = new Cluster();
        Post postOne = new Post();

        //And
        Mockito.when(mockIssueRepositoryService.checkClusterExist(clusterOne.getClusterID())).thenReturn(true);
        Mockito.when(mockIssueRepositoryService.checkPostExist(postOne.getPostID())).thenReturn(true);

        //And
        Administrator admin = new Administrator("AdminUser");

        //When
        issueManager.addPostToCluser(admin, clusterOne, postOne);

        //Then
        verify(mockIssueRepositoryService).addPostToCluster(clusterOne, postOne);
    }

    @Test
    public void shouldAllowAdminUserToAddForumPostThatExistsInOtherClusterIntoCluster(){
        //Given
        Cluster clusterOne = new Cluster();
        Cluster clusterTwo = new Cluster();
        Post postOne = new Post();
        Administrator admin = new Administrator("AdminUser");

        //And
        Mockito.when(mockIssueRepositoryService.checkClusterExist(clusterOne.getClusterID())).thenReturn(true);
        Mockito.when(mockIssueRepositoryService.checkPostExist(postOne.getPostID())).thenReturn(true);

        //And
        issueManager.addPostToCluser(admin, clusterOne, postOne);

        //And
        issueManager.addPostToCluser(admin, clusterTwo, postOne);

        //Then
        verify(mockIssueRepositoryService).addPostToCluster(clusterOne, postOne);
        verify(mockIssueRepositoryService).addPostToCluster(clusterTwo, postOne);
    }

    @Test
    public void shouldNotAllowNonAdminUsersToAddAForumPostToACluster(){
        //Given
        Cluster clusterOne = new Cluster();
        Post postOne = new Post();
        Developer developer = new Developer("DeveloperOne");

        //And
        Mockito.when(mockIssueRepositoryService.checkClusterExist(clusterOne.getClusterID())).thenReturn(true);
        Mockito.when(mockIssueRepositoryService.checkPostExist(postOne.getPostID())).thenReturn(true);

        //And
        expected.expect(RuntimeException.class);
        expected.expectMessage(ApplicationConstants.DOES_NOT_PRIVILEGE_MSG);

        //When
        issueManager.addPostToCluser(developer, clusterOne, postOne);
    }
    
    @Test
    public void shouldNotAllowNonAdminUsersToRemoveAForumPostFromCluster(){
        //Given
        Cluster clusterOne = new Cluster();
        Post postOne = new Post();

        //And
        expected.expect(RuntimeException.class);
        expected.expectMessage(ApplicationConstants.DOES_NOT_PRIVILEGE_MSG);

        //And
        Developer developer = new Developer("DeveloperOne");

        //Then
        issueManager.removePostFromCluster(developer, clusterOne, postOne);
    }

    @Test
    public void shouldNotAllowAdminUserToAddForumPostIntoClusterWhenForumPostIsAlreadyInCluster(){
        //Given
        Cluster clusterOne = new Cluster();
        Post postOne = new Post();
        Administrator admin = new Administrator("AdminUser");

        //And
        when(mockIssueRepositoryService.checkClusterExist(clusterOne.getClusterID())).thenReturn(true);
        when(mockIssueRepositoryService.checkPostExist(postOne.getPostID())).thenReturn(true);

        Mockito.doNothing().doThrow(new RuntimeException(ApplicationConstants.CLUSTER_ALREADY_HAS_THE_POST_MSG))
        .when(mockIssueRepositoryService).addPostToCluster(clusterOne, postOne);

        issueManager.addPostToCluser(admin, clusterOne, postOne);

        //And
        expected.expect(RuntimeException.class);
        expected.expectMessage(ApplicationConstants.CLUSTER_ALREADY_HAS_THE_POST_MSG);

        //When
        issueManager.addPostToCluser(admin, clusterOne, postOne);
    }
    
    @Test
    public void shouldNotAllowAdminUserToAddForumPostIntoNonExistingCluster(){
        //Given
        Cluster clusterOne = new Cluster();
        Post postOne = new Post();
        Administrator admin = new Administrator("AdminUser");

        //And
        Mockito.when(mockIssueRepositoryService.checkClusterExist(clusterOne.getClusterID())).thenReturn(false);

        //And
        expected.expect(RuntimeException.class);
        expected.expectMessage(ApplicationConstants.CLUSTER_DOES_NOT_EXSIST_MSG);

        //When
        issueManager.addPostToCluser(admin, clusterOne, postOne);
    }

    @Test
    public void shouldNotAllowAdminUserToAddNonexistingForumPostIntoCluster(){
        //Given
        Cluster clusterOne = new Cluster();
        Post postOne = new Post();
        Administrator admin = new Administrator("AdminUser");

        //And
        Mockito.when(mockIssueRepositoryService.checkClusterExist(clusterOne.getClusterID())).thenReturn(true);
        Mockito.when(mockIssueRepositoryService.checkPostExist(postOne.getPostID())).thenReturn(false);

        //And
        expected.expect(RuntimeException.class);
        expected.expectMessage(ApplicationConstants.FORUM_POST_DOES_NOT_EXSIST_MSG);

        //When
        issueManager.addPostToCluser(admin, clusterOne, postOne);
    }
    
    @Test
    public void shouldNotAllowAdminUserToAddNoneexistingForumPostIntoNonexistingCluster(){
        //Given
        Cluster clusterOne = new Cluster();
        Post postOne = new Post();
        Administrator admin = new Administrator("AdminUser");

        //And
        Mockito.when(mockIssueRepositoryService.checkClusterExist(clusterOne.getClusterID())).thenReturn(false);
        Mockito.when(mockIssueRepositoryService.checkPostExist(postOne.getPostID())).thenReturn(false);

        //And
        expected.expect(RuntimeException.class);
        
        //And (because this condition fires first)
        expected.expectMessage(ApplicationConstants.CLUSTER_DOES_NOT_EXSIST_MSG);

        //When
        issueManager.addPostToCluser(admin, clusterOne, postOne);
    }

    @Test
    public void shouldAllowAdminUserToRemoveForumPostFromCluster() {
        // Given
        Cluster clusterOne = new Cluster();
        Post postOne = new Post();

        // And
        Mockito.when(mockIssueRepositoryService.checkClusterExist(clusterOne.getClusterID())).thenReturn(true);
        Mockito.when(mockIssueRepositoryService.checkPostExist(postOne.getPostID())).thenReturn(true);

        // And
        Administrator admin = new Administrator("AdminUser");

        // When
        issueManager.removePostFromCluster(admin, clusterOne, postOne);

        // Then
        verify(mockIssueRepositoryService).removePostFromCluster(clusterOne, postOne);
    }

}
