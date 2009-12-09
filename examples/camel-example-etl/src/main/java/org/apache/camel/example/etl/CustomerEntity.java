begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.etl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|etl
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Entity
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|GeneratedValue
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Id
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_comment
comment|/**  * An example entity bean which can be marshalled to/from XML  *   * @version $Revision$  */
end_comment

begin_class
annotation|@
name|Entity
argument_list|(
name|name
operator|=
literal|"customer"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"customer"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|CustomerEntity
specifier|public
class|class
name|CustomerEntity
block|{
annotation|@
name|XmlAttribute
DECL|field|id
specifier|private
name|Long
name|id
decl_stmt|;
DECL|field|userName
specifier|private
name|String
name|userName
decl_stmt|;
DECL|field|firstName
specifier|private
name|String
name|firstName
decl_stmt|;
DECL|field|surname
specifier|private
name|String
name|surname
decl_stmt|;
DECL|field|street
specifier|private
name|String
name|street
decl_stmt|;
DECL|field|city
specifier|private
name|String
name|city
decl_stmt|;
DECL|field|zip
specifier|private
name|String
name|zip
decl_stmt|;
DECL|field|phone
specifier|private
name|String
name|phone
decl_stmt|;
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Customer[userName: "
operator|+
name|userName
operator|+
literal|" firstName: "
operator|+
name|firstName
operator|+
literal|" surname: "
operator|+
name|surname
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Id
annotation|@
name|GeneratedValue
DECL|method|getId ()
specifier|public
name|Long
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (Long id)
specifier|public
name|void
name|setId
parameter_list|(
name|Long
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getCity ()
specifier|public
name|String
name|getCity
parameter_list|()
block|{
return|return
name|city
return|;
block|}
DECL|method|setCity (String city)
specifier|public
name|void
name|setCity
parameter_list|(
name|String
name|city
parameter_list|)
block|{
name|this
operator|.
name|city
operator|=
name|city
expr_stmt|;
block|}
DECL|method|getFirstName ()
specifier|public
name|String
name|getFirstName
parameter_list|()
block|{
return|return
name|firstName
return|;
block|}
DECL|method|setFirstName (String firstName)
specifier|public
name|void
name|setFirstName
parameter_list|(
name|String
name|firstName
parameter_list|)
block|{
name|this
operator|.
name|firstName
operator|=
name|firstName
expr_stmt|;
block|}
DECL|method|getPhone ()
specifier|public
name|String
name|getPhone
parameter_list|()
block|{
return|return
name|phone
return|;
block|}
DECL|method|setPhone (String phone)
specifier|public
name|void
name|setPhone
parameter_list|(
name|String
name|phone
parameter_list|)
block|{
name|this
operator|.
name|phone
operator|=
name|phone
expr_stmt|;
block|}
DECL|method|getStreet ()
specifier|public
name|String
name|getStreet
parameter_list|()
block|{
return|return
name|street
return|;
block|}
DECL|method|setStreet (String street)
specifier|public
name|void
name|setStreet
parameter_list|(
name|String
name|street
parameter_list|)
block|{
name|this
operator|.
name|street
operator|=
name|street
expr_stmt|;
block|}
DECL|method|getSurname ()
specifier|public
name|String
name|getSurname
parameter_list|()
block|{
return|return
name|surname
return|;
block|}
DECL|method|setSurname (String surname)
specifier|public
name|void
name|setSurname
parameter_list|(
name|String
name|surname
parameter_list|)
block|{
name|this
operator|.
name|surname
operator|=
name|surname
expr_stmt|;
block|}
DECL|method|getUserName ()
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|userName
return|;
block|}
DECL|method|setUserName (String userName)
specifier|public
name|void
name|setUserName
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
block|}
DECL|method|getZip ()
specifier|public
name|String
name|getZip
parameter_list|()
block|{
return|return
name|zip
return|;
block|}
DECL|method|setZip (String zip)
specifier|public
name|void
name|setZip
parameter_list|(
name|String
name|zip
parameter_list|)
block|{
name|this
operator|.
name|zip
operator|=
name|zip
expr_stmt|;
block|}
block|}
end_class

end_unit

