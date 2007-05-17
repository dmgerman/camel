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
name|javax
operator|.
name|persistence
operator|.
name|Transient
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
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_comment
comment|/**  * @version $Revision: $  */
end_comment

begin_class
DECL|class|TemporalEntity
specifier|public
specifier|abstract
class|class
name|TemporalEntity
extends|extends
name|EntitySupport
block|{
DECL|field|timeStarted
specifier|private
name|Date
name|timeStarted
decl_stmt|;
DECL|field|timeCompleted
specifier|private
name|Date
name|timeCompleted
decl_stmt|;
annotation|@
name|Transient
DECL|method|isStarted ()
specifier|public
name|boolean
name|isStarted
parameter_list|()
block|{
return|return
name|timeStarted
operator|!=
literal|null
return|;
block|}
annotation|@
name|Transient
DECL|method|isCompleted ()
specifier|public
name|boolean
name|isCompleted
parameter_list|()
block|{
return|return
name|timeCompleted
operator|!=
literal|null
return|;
block|}
DECL|method|getTimeStarted ()
specifier|public
name|Date
name|getTimeStarted
parameter_list|()
block|{
return|return
name|timeStarted
return|;
block|}
DECL|method|setTimeStarted (Date timeStarted)
specifier|public
name|void
name|setTimeStarted
parameter_list|(
name|Date
name|timeStarted
parameter_list|)
block|{
name|this
operator|.
name|timeStarted
operator|=
name|timeStarted
expr_stmt|;
block|}
DECL|method|getTimeCompleted ()
specifier|public
name|Date
name|getTimeCompleted
parameter_list|()
block|{
return|return
name|timeCompleted
return|;
block|}
DECL|method|setTimeCompleted (Date timeCompleted)
specifier|public
name|void
name|setTimeCompleted
parameter_list|(
name|Date
name|timeCompleted
parameter_list|)
block|{
name|this
operator|.
name|timeCompleted
operator|=
name|timeCompleted
expr_stmt|;
block|}
block|}
end_class

end_unit

