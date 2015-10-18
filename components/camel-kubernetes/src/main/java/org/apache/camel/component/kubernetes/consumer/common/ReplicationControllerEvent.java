begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.kubernetes.consumer.common
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kubernetes
operator|.
name|consumer
operator|.
name|common
package|;
end_package

begin_import
import|import
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|api
operator|.
name|model
operator|.
name|ReplicationController
import|;
end_import

begin_import
import|import
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|client
operator|.
name|Watcher
operator|.
name|Action
import|;
end_import

begin_class
DECL|class|ReplicationControllerEvent
specifier|public
class|class
name|ReplicationControllerEvent
block|{
DECL|field|action
specifier|private
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|client
operator|.
name|Watcher
operator|.
name|Action
name|action
decl_stmt|;
DECL|field|replicationController
specifier|private
name|ReplicationController
name|replicationController
decl_stmt|;
DECL|method|ReplicationControllerEvent (Action action, ReplicationController rc)
specifier|public
name|ReplicationControllerEvent
parameter_list|(
name|Action
name|action
parameter_list|,
name|ReplicationController
name|rc
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|action
operator|=
name|action
expr_stmt|;
name|this
operator|.
name|replicationController
operator|=
name|rc
expr_stmt|;
block|}
DECL|method|getAction ()
specifier|public
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|client
operator|.
name|Watcher
operator|.
name|Action
name|getAction
parameter_list|()
block|{
return|return
name|action
return|;
block|}
DECL|method|setAction (io.fabric8.kubernetes.client.Watcher.Action action)
specifier|public
name|void
name|setAction
parameter_list|(
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|client
operator|.
name|Watcher
operator|.
name|Action
name|action
parameter_list|)
block|{
name|this
operator|.
name|action
operator|=
name|action
expr_stmt|;
block|}
DECL|method|getReplicationController ()
specifier|public
name|ReplicationController
name|getReplicationController
parameter_list|()
block|{
return|return
name|replicationController
return|;
block|}
DECL|method|setReplicationController (ReplicationController replicationController)
specifier|public
name|void
name|setReplicationController
parameter_list|(
name|ReplicationController
name|replicationController
parameter_list|)
block|{
name|this
operator|.
name|replicationController
operator|=
name|replicationController
expr_stmt|;
block|}
block|}
end_class

end_unit

