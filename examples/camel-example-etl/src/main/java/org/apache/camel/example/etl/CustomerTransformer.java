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
name|Converter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|orm
operator|.
name|jpa
operator|.
name|JpaTemplate
import|;
end_import

begin_comment
comment|/**  * A Message Transformer of an XML document to a Customer entity bean  *   * @version $Revision: 1.1 $  */
end_comment

begin_comment
comment|// START SNIPPET: example
end_comment

begin_class
annotation|@
name|Converter
DECL|class|CustomerTransformer
specifier|public
class|class
name|CustomerTransformer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|CustomerTransformer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|template
specifier|private
name|JpaTemplate
name|template
decl_stmt|;
DECL|method|CustomerTransformer (JpaTemplate template)
specifier|public
name|CustomerTransformer
parameter_list|(
name|JpaTemplate
name|template
parameter_list|)
block|{
name|this
operator|.
name|template
operator|=
name|template
expr_stmt|;
block|}
comment|/**      * A transformation method to convert a person document into a customer      * entity      */
annotation|@
name|Converter
DECL|method|toCustomer (PersonDocument doc)
specifier|public
name|CustomerEntity
name|toCustomer
parameter_list|(
name|PersonDocument
name|doc
parameter_list|)
block|{
name|String
name|user
init|=
name|doc
operator|.
name|getUser
argument_list|()
decl_stmt|;
name|CustomerEntity
name|customer
init|=
name|findCustomerByName
argument_list|(
name|user
argument_list|)
decl_stmt|;
comment|// lets convert information from the document into the entity bean
name|customer
operator|.
name|setFirstName
argument_list|(
name|doc
operator|.
name|getFirstName
argument_list|()
argument_list|)
expr_stmt|;
name|customer
operator|.
name|setSurname
argument_list|(
name|doc
operator|.
name|getLastName
argument_list|()
argument_list|)
expr_stmt|;
name|customer
operator|.
name|setCity
argument_list|(
name|doc
operator|.
name|getCity
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Created customer: "
operator|+
name|customer
argument_list|)
expr_stmt|;
return|return
name|customer
return|;
block|}
comment|/**      * Finds a customer for the given username, or creates and inserts a new one      */
DECL|method|findCustomerByName (String user)
specifier|protected
name|CustomerEntity
name|findCustomerByName
parameter_list|(
name|String
name|user
parameter_list|)
block|{
name|List
argument_list|<
name|CustomerEntity
argument_list|>
name|list
init|=
name|template
operator|.
name|find
argument_list|(
literal|"select x from "
operator|+
name|CustomerEntity
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" x where x.userName = ?1"
argument_list|,
name|user
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|CustomerEntity
name|answer
init|=
operator|new
name|CustomerEntity
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setUserName
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|template
operator|.
name|persist
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
else|else
block|{
return|return
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
block|}
block|}
end_class

begin_comment
comment|// END SNIPPET: example
end_comment

end_unit

