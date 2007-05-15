begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.bam.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|bam
operator|.
name|model
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
name|bam
operator|.
name|ProcessDefinition
import|;
end_import

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
name|NamedQuery
import|;
end_import

begin_comment
comment|/**  * Represents a activity which is typically a system or could be an endpoint  *  * @version $Revision: $  */
end_comment

begin_class
annotation|@
name|Entity
annotation|@
name|NamedQuery
argument_list|(
name|name
operator|=
literal|"findByName"
argument_list|,
name|query
operator|=
literal|"select x from org.apache.camel.bam.model.Activity where x.name = ?1"
argument_list|)
DECL|class|Activity
specifier|public
class|class
name|Activity
block|{
DECL|field|expectedMessages
specifier|private
name|int
name|expectedMessages
init|=
literal|1
decl_stmt|;
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|process
specifier|private
name|ProcessDefinition
name|process
decl_stmt|;
DECL|method|Activity (ProcessDefinition process)
specifier|public
name|Activity
parameter_list|(
name|ProcessDefinition
name|process
parameter_list|)
block|{
name|this
operator|.
name|process
operator|=
name|process
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getExpectedMessages ()
specifier|public
name|int
name|getExpectedMessages
parameter_list|()
block|{
return|return
name|expectedMessages
return|;
block|}
DECL|method|setExpectedMessages (int expectedMessages)
specifier|public
name|void
name|setExpectedMessages
parameter_list|(
name|int
name|expectedMessages
parameter_list|)
block|{
name|this
operator|.
name|expectedMessages
operator|=
name|expectedMessages
expr_stmt|;
block|}
comment|/**      * Perform any assertions after the state has been updated      */
DECL|method|process (ActivityState activityState, Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|ActivityState
name|activityState
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// TODO
block|}
block|}
end_class

end_unit

