begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Salesforce DTO generated by camel-salesforce-maven-plugin  */
end_comment

begin_package
DECL|package|$packageName
package|package
name|$packageName
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonCreator
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonValue
import|;
end_import

begin_comment
comment|/**  * Salesforce Enumeration DTO for picklist PickListSlash  */
end_comment

begin_enum
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.CamelSalesforceMojo"
argument_list|)
DECL|enum|Case_PickListSlashEnum
specifier|public
enum|enum
name|Case_PickListSlashEnum
block|{
comment|// Acciones relacionadas con cotizaciones y/o avisos de entrega
DECL|enumConstant|ACCIONES_RELACIONADAS_CON_COTIZACIONES_Y_O_AVISOS_DE_ENTREGA
name|ACCIONES_RELACIONADAS_CON_COTIZACIONES_Y_O_AVISOS_DE_ENTREGA
argument_list|(
literal|"Acciones relacionadas con cotizaciones y/o avisos de entrega"
argument_list|)
block|;
DECL|field|value
specifier|final
name|String
name|value
decl_stmt|;
DECL|method|Case_PickListSlashEnum (String value)
specifier|private
name|Case_PickListSlashEnum
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
annotation|@
name|JsonValue
DECL|method|value ()
specifier|public
name|String
name|value
parameter_list|()
block|{
return|return
name|this
operator|.
name|value
return|;
block|}
annotation|@
name|JsonCreator
DECL|method|fromValue (String value)
specifier|public
specifier|static
name|Case_PickListSlashEnum
name|fromValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
for|for
control|(
name|Case_PickListSlashEnum
name|e
range|:
name|Case_PickListSlashEnum
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|e
operator|.
name|value
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|e
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|value
argument_list|)
throw|;
block|}
block|}
end_enum

end_unit

