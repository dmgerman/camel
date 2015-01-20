begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Processor
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
name|processor
operator|.
name|RollbackProcessor
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
name|spi
operator|.
name|Label
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
name|spi
operator|.
name|RouteContext
import|;
end_import

begin_comment
comment|/**  * Forces a rollback by stopping routing the message  */
end_comment

begin_class
annotation|@
name|Label
argument_list|(
literal|"eip,routing"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"rollback"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|RollbackDefinition
specifier|public
class|class
name|RollbackDefinition
extends|extends
name|NoOutputDefinition
argument_list|<
name|RollbackDefinition
argument_list|>
block|{
annotation|@
name|XmlAttribute
DECL|field|markRollbackOnly
specifier|private
name|Boolean
name|markRollbackOnly
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|markRollbackOnlyLast
specifier|private
name|Boolean
name|markRollbackOnlyLast
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|message
specifier|private
name|String
name|message
decl_stmt|;
DECL|method|RollbackDefinition ()
specifier|public
name|RollbackDefinition
parameter_list|()
block|{     }
DECL|method|RollbackDefinition (String message)
specifier|public
name|RollbackDefinition
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
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
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
return|return
literal|"Rollback["
operator|+
name|message
operator|+
literal|"]"
return|;
block|}
else|else
block|{
return|return
literal|"Rollback"
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
literal|"rollback"
return|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
comment|// validate that only either mark rollbacks is chosen and not both
if|if
condition|(
name|isMarkRollbackOnly
argument_list|()
operator|&&
name|isMarkRollbackOnlyLast
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Only either one of markRollbackOnly and markRollbackOnlyLast is possible to select as true"
argument_list|)
throw|;
block|}
name|RollbackProcessor
name|answer
init|=
operator|new
name|RollbackProcessor
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setMarkRollbackOnly
argument_list|(
name|isMarkRollbackOnly
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setMarkRollbackOnlyLast
argument_list|(
name|isMarkRollbackOnlyLast
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|getMessage ()
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|message
return|;
block|}
comment|/**      * Message to use in rollback exception      */
DECL|method|setMessage (String message)
specifier|public
name|void
name|setMessage
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
DECL|method|getMarkRollbackOnly ()
specifier|public
name|Boolean
name|getMarkRollbackOnly
parameter_list|()
block|{
return|return
name|markRollbackOnly
return|;
block|}
comment|/**      * Mark the transaction for rollback only (cannot be overruled to commit)      */
DECL|method|setMarkRollbackOnly (Boolean markRollbackOnly)
specifier|public
name|void
name|setMarkRollbackOnly
parameter_list|(
name|Boolean
name|markRollbackOnly
parameter_list|)
block|{
name|this
operator|.
name|markRollbackOnly
operator|=
name|markRollbackOnly
expr_stmt|;
block|}
DECL|method|isMarkRollbackOnly ()
specifier|public
name|boolean
name|isMarkRollbackOnly
parameter_list|()
block|{
return|return
name|markRollbackOnly
operator|!=
literal|null
operator|&&
name|markRollbackOnly
return|;
block|}
DECL|method|getMarkRollbackOnlyLast ()
specifier|public
name|Boolean
name|getMarkRollbackOnlyLast
parameter_list|()
block|{
return|return
name|markRollbackOnlyLast
return|;
block|}
comment|/**      * Mark only last sub transaction for rollback only.      *<p/>      * When using sub transactions (if the transaction manager support this)      */
DECL|method|setMarkRollbackOnlyLast (Boolean markRollbackOnlyLast)
specifier|public
name|void
name|setMarkRollbackOnlyLast
parameter_list|(
name|Boolean
name|markRollbackOnlyLast
parameter_list|)
block|{
name|this
operator|.
name|markRollbackOnlyLast
operator|=
name|markRollbackOnlyLast
expr_stmt|;
block|}
DECL|method|isMarkRollbackOnlyLast ()
specifier|public
name|boolean
name|isMarkRollbackOnlyLast
parameter_list|()
block|{
return|return
name|markRollbackOnlyLast
operator|!=
literal|null
operator|&&
name|markRollbackOnlyLast
return|;
block|}
block|}
end_class

end_unit

