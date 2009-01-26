begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
package|;
end_package

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|GenericFileDefaultBinding
specifier|public
class|class
name|GenericFileDefaultBinding
implements|implements
name|GenericFileBinding
block|{
DECL|field|body
specifier|private
name|Object
name|body
decl_stmt|;
DECL|method|getBody (GenericFile genericFile)
specifier|public
name|Object
name|getBody
parameter_list|(
name|GenericFile
name|genericFile
parameter_list|)
block|{
return|return
name|body
return|;
block|}
DECL|method|setBody (GenericFile genericFile, Object body)
specifier|public
name|void
name|setBody
parameter_list|(
name|GenericFile
name|genericFile
parameter_list|,
name|Object
name|body
parameter_list|)
block|{
name|this
operator|.
name|body
operator|=
name|body
expr_stmt|;
block|}
block|}
end_class

end_unit

