begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.jpa
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|osgi
operator|.
name|jpa
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
name|Id
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Table
import|;
end_import

begin_comment
comment|/**  * Represents a task which is added to the database, then removed from the database when it is consumed  *  * @version   */
end_comment

begin_class
annotation|@
name|Entity
annotation|@
name|Table
argument_list|(
name|name
operator|=
literal|"SENDEMAIL"
argument_list|)
DECL|class|SendEmail
specifier|public
class|class
name|SendEmail
block|{
DECL|field|id
specifier|private
name|Long
name|id
decl_stmt|;
DECL|field|address
specifier|private
name|String
name|address
decl_stmt|;
DECL|method|SendEmail ()
specifier|public
name|SendEmail
parameter_list|()
block|{     }
DECL|method|SendEmail (Long id,String address)
specifier|public
name|SendEmail
parameter_list|(
name|Long
name|id
parameter_list|,
name|String
name|address
parameter_list|)
block|{
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
DECL|method|SendEmail (String address)
specifier|public
name|SendEmail
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|setAddress
argument_list|(
name|address
argument_list|)
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
literal|"SendEmail[id: "
operator|+
name|getId
argument_list|()
operator|+
literal|" address: "
operator|+
name|getAddress
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Id
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
DECL|method|getAddress ()
specifier|public
name|String
name|getAddress
parameter_list|()
block|{
return|return
name|address
return|;
block|}
DECL|method|setAddress (String address)
specifier|public
name|void
name|setAddress
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|this
operator|.
name|address
operator|=
name|address
expr_stmt|;
block|}
block|}
end_class

end_unit

