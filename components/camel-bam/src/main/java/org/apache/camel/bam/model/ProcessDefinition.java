begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|List
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
name|persistence
operator|.
name|Table
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|UniqueConstraint
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
name|util
operator|.
name|ObjectHelper
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
comment|/**  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|Entity
annotation|@
name|Table
argument_list|(
name|name
operator|=
literal|"CAMEL_PROCESSDEFINITION"
argument_list|,
name|uniqueConstraints
operator|=
annotation|@
name|UniqueConstraint
argument_list|(
name|columnNames
operator|=
block|{
literal|"name"
block|}
argument_list|)
argument_list|)
DECL|class|ProcessDefinition
specifier|public
class|class
name|ProcessDefinition
extends|extends
name|EntitySupport
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
name|ProcessDefinition
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
comment|// This crap is required to work around a bug in hibernate
annotation|@
name|Override
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
name|super
operator|.
name|getId
argument_list|()
return|;
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
DECL|method|getRefreshedProcessDefinition (JpaTemplate template, ProcessDefinition definition)
specifier|public
specifier|static
name|ProcessDefinition
name|getRefreshedProcessDefinition
parameter_list|(
name|JpaTemplate
name|template
parameter_list|,
name|ProcessDefinition
name|definition
parameter_list|)
block|{
comment|// TODO refresh doesn't tend to work - maybe its a spring thing?
comment|// template.refresh(definition);
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|definition
argument_list|,
literal|"definition"
argument_list|)
expr_stmt|;
name|Long
name|id
init|=
name|definition
operator|.
name|getId
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"No primary key is available!"
argument_list|)
expr_stmt|;
return|return
name|findOrCreateProcessDefinition
argument_list|(
name|template
argument_list|,
name|definition
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
name|definition
operator|=
name|template
operator|.
name|find
argument_list|(
name|ProcessDefinition
operator|.
name|class
argument_list|,
name|id
argument_list|)
expr_stmt|;
return|return
name|definition
return|;
block|}
DECL|method|findOrCreateProcessDefinition (JpaTemplate template, String processName)
specifier|public
specifier|static
name|ProcessDefinition
name|findOrCreateProcessDefinition
parameter_list|(
name|JpaTemplate
name|template
parameter_list|,
name|String
name|processName
parameter_list|)
block|{
name|List
argument_list|<
name|ProcessDefinition
argument_list|>
name|list
init|=
name|template
operator|.
name|find
argument_list|(
literal|"select x from "
operator|+
name|ProcessDefinition
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" x where x.name = ?1"
argument_list|,
name|processName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
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
else|else
block|{
name|ProcessDefinition
name|answer
init|=
operator|new
name|ProcessDefinition
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setName
argument_list|(
name|processName
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
block|}
block|}
end_class

end_unit

