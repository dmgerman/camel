begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.dataformat.bindy.model.padding
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|model
operator|.
name|padding
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|annotation
operator|.
name|CsvRecord
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|annotation
operator|.
name|DataField
import|;
end_import

begin_class
annotation|@
name|CsvRecord
argument_list|(
name|separator
operator|=
literal|","
argument_list|)
DECL|class|Unity
specifier|public
class|class
name|Unity
block|{
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|1
argument_list|,
name|pattern
operator|=
literal|"000"
argument_list|)
DECL|field|mandant
specifier|public
name|float
name|mandant
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|2
argument_list|,
name|pattern
operator|=
literal|"000"
argument_list|)
DECL|field|receiver
specifier|public
name|float
name|receiver
decl_stmt|;
DECL|method|getMandant ()
specifier|public
name|float
name|getMandant
parameter_list|()
block|{
return|return
name|mandant
return|;
block|}
DECL|method|setMandant (float mandant)
specifier|public
name|void
name|setMandant
parameter_list|(
name|float
name|mandant
parameter_list|)
block|{
name|this
operator|.
name|mandant
operator|=
name|mandant
expr_stmt|;
block|}
DECL|method|getReceiver ()
specifier|public
name|float
name|getReceiver
parameter_list|()
block|{
return|return
name|receiver
return|;
block|}
DECL|method|setReceiver (float receiver)
specifier|public
name|void
name|setReceiver
parameter_list|(
name|float
name|receiver
parameter_list|)
block|{
name|this
operator|.
name|receiver
operator|=
name|receiver
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Unity [mandant="
operator|+
name|mandant
operator|+
literal|", receiver="
operator|+
name|receiver
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

