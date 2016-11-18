begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.firebase.data
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|firebase
operator|.
name|data
package|;
end_package

begin_comment
comment|/**  * Operations associated with the messages.  */
end_comment

begin_enum
DECL|enum|Operation
specifier|public
enum|enum
name|Operation
block|{
DECL|enumConstant|CHILD_ADD
name|CHILD_ADD
block|,
DECL|enumConstant|CHILD_CHANGED
name|CHILD_CHANGED
block|,
DECL|enumConstant|CHILD_REMOVED
name|CHILD_REMOVED
block|,
DECL|enumConstant|CHILD_MOVED
name|CHILD_MOVED
block|,
DECL|enumConstant|CANCELLED
name|CANCELLED
block|}
end_enum

end_unit

