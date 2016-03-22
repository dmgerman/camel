begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.infinispan.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|infinispan
operator|.
name|util
package|;
end_package

begin_comment
comment|/**  * @author Martin Gencur  */
end_comment

begin_interface
DECL|interface|Condition
specifier|public
interface|interface
name|Condition
block|{
DECL|method|isSatisfied ()
name|boolean
name|isSatisfied
parameter_list|()
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

