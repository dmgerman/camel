begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.kafka
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kafka
package|;
end_package

begin_import
import|import
name|kafka
operator|.
name|producer
operator|.
name|Partitioner
import|;
end_import

begin_import
import|import
name|kafka
operator|.
name|utils
operator|.
name|VerifiableProperties
import|;
end_import

begin_comment
comment|/**  * @author Stephen Samuel  */
end_comment

begin_class
DECL|class|SimplePartitioner
specifier|public
class|class
name|SimplePartitioner
implements|implements
name|Partitioner
argument_list|<
name|String
argument_list|>
block|{
DECL|method|SimplePartitioner (VerifiableProperties props)
specifier|public
name|SimplePartitioner
parameter_list|(
name|VerifiableProperties
name|props
parameter_list|)
block|{     }
comment|/**      * Uses the key to calculate a partition bucket id for routing      * the data to the appropriate broker partition      *      * @return an integer between 0 and numPartitions-1      */
annotation|@
name|Override
DECL|method|partition (String key, int numPartitions)
specifier|public
name|int
name|partition
parameter_list|(
name|String
name|key
parameter_list|,
name|int
name|numPartitions
parameter_list|)
block|{
return|return
name|key
operator|.
name|hashCode
argument_list|()
operator|%
name|numPartitions
return|;
block|}
block|}
end_class

end_unit

