begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.itest.highvolume
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|highvolume
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|EndpointInject
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
name|Exchange
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
name|Handler
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
name|ProducerTemplate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_class
DECL|class|Messages
specifier|public
class|class
name|Messages
block|{
annotation|@
name|EndpointInject
argument_list|(
name|ref
operator|=
literal|"Direct"
argument_list|)
DECL|field|template
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|ref
operator|=
literal|"JmsQueueProducer"
argument_list|)
DECL|field|templateActiveMq
name|ProducerTemplate
name|templateActiveMq
decl_stmt|;
DECL|method|splitString (Exchange exchange)
specifier|public
name|void
name|splitString
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|messages
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|String
index|[]
name|result
init|=
name|messages
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|result
operator|.
name|length
condition|;
name|x
operator|++
control|)
block|{
name|templateActiveMq
operator|.
name|sendBody
argument_list|(
name|result
index|[
name|x
index|]
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|splitObject (Exchange exchange)
specifier|public
name|void
name|splitObject
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|List
argument_list|<
name|Person
argument_list|>
name|persons
init|=
operator|(
name|List
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
for|for
control|(
name|Person
name|person
range|:
name|persons
control|)
block|{
name|templateActiveMq
operator|.
name|sendBody
argument_list|(
name|person
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|generateString (Exchange exchange)
specifier|public
name|void
name|generateString
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|StringBuilder
name|messages
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
literal|20000
condition|;
name|i
operator|++
control|)
block|{
name|messages
operator|.
name|append
argument_list|(
literal|"Test Message: "
operator|+
name|i
operator|+
literal|","
argument_list|)
expr_stmt|;
block|}
name|template
operator|.
name|sendBody
argument_list|(
name|messages
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|generateObject (Exchange exchange)
specifier|public
name|void
name|generateObject
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Person
name|person
decl_stmt|;
name|List
argument_list|<
name|Person
argument_list|>
name|persons
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
literal|15000
condition|;
name|i
operator|++
control|)
block|{
name|person
operator|=
name|createPerson
argument_list|()
expr_stmt|;
name|person
operator|.
name|setId
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|persons
operator|.
name|add
argument_list|(
name|person
argument_list|)
expr_stmt|;
block|}
name|template
operator|.
name|sendBody
argument_list|(
name|persons
argument_list|)
expr_stmt|;
block|}
DECL|method|createPerson ()
specifier|private
specifier|static
name|Person
name|createPerson
parameter_list|()
block|{
name|Person
name|person
init|=
operator|new
name|Person
argument_list|()
decl_stmt|;
name|Address
name|address
init|=
operator|new
name|Address
argument_list|()
decl_stmt|;
name|Contact
name|contact
init|=
operator|new
name|Contact
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Contact
argument_list|>
name|contacts
init|=
operator|new
name|ArrayList
argument_list|<
name|Contact
argument_list|>
argument_list|()
decl_stmt|;
name|address
operator|.
name|setLocality
argument_list|(
literal|"ApacheWorld"
argument_list|)
expr_stmt|;
name|address
operator|.
name|setNumber
argument_list|(
literal|"666"
argument_list|)
expr_stmt|;
name|address
operator|.
name|setPostalCode
argument_list|(
literal|"B-1000"
argument_list|)
expr_stmt|;
name|address
operator|.
name|setStreet
argument_list|(
literal|"CommitterStreet"
argument_list|)
expr_stmt|;
comment|// First contact
name|contact
operator|.
name|setCompany
argument_list|(
literal|"Fuse"
argument_list|)
expr_stmt|;
name|contact
operator|.
name|setFirstName
argument_list|(
literal|"Claus"
argument_list|)
expr_stmt|;
name|contact
operator|.
name|setLastName
argument_list|(
literal|"Ibsen"
argument_list|)
expr_stmt|;
name|contact
operator|.
name|setTitle
argument_list|(
literal|"Camel Designer"
argument_list|)
expr_stmt|;
name|contacts
operator|.
name|add
argument_list|(
name|contact
argument_list|)
expr_stmt|;
comment|// 2nd contact
name|contact
operator|.
name|setCompany
argument_list|(
literal|"Fuse"
argument_list|)
expr_stmt|;
name|contact
operator|.
name|setFirstName
argument_list|(
literal|"Guillaume"
argument_list|)
expr_stmt|;
name|contact
operator|.
name|setLastName
argument_list|(
literal|"Nodet"
argument_list|)
expr_stmt|;
name|contact
operator|.
name|setTitle
argument_list|(
literal|"Karaf Designer"
argument_list|)
expr_stmt|;
name|contacts
operator|.
name|add
argument_list|(
name|contact
argument_list|)
expr_stmt|;
comment|// 3rd contact
name|contact
operator|.
name|setCompany
argument_list|(
literal|"Apache Foundation"
argument_list|)
expr_stmt|;
name|contact
operator|.
name|setFirstName
argument_list|(
literal|"Jean-Baptiste"
argument_list|)
expr_stmt|;
name|contact
operator|.
name|setLastName
argument_list|(
literal|"D'Onofré"
argument_list|)
expr_stmt|;
name|contact
operator|.
name|setTitle
argument_list|(
literal|"ServiceMix committer"
argument_list|)
expr_stmt|;
name|contacts
operator|.
name|add
argument_list|(
name|contact
argument_list|)
expr_stmt|;
name|person
operator|.
name|setFirstName
argument_list|(
literal|"Charles"
argument_list|)
expr_stmt|;
name|person
operator|.
name|setLastName
argument_list|(
literal|"Moulliard"
argument_list|)
expr_stmt|;
name|person
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|person
operator|.
name|setContacts
argument_list|(
name|contacts
argument_list|)
expr_stmt|;
return|return
name|person
return|;
block|}
block|}
end_class

end_unit

