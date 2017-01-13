begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_interface
DECL|interface|ThrottingExceptionHalfOpenHandler
specifier|public
interface|interface
name|ThrottingExceptionHalfOpenHandler
block|{
DECL|method|isReadyToBeClosed ()
name|boolean
name|isReadyToBeClosed
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

