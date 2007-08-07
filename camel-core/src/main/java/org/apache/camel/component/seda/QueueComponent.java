begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.seda
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|seda
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

begin_comment
comment|/**  * An implementation of the<a href="http://activemq.apache.org/camel/queue.html">Queue components</a>  * for asynchronous SEDA exchanges on a {@link BlockingQueue} within a CamelContext  *  * @org.apache.xbean.XBean  * @version $Revision: 519973 $  */
end_comment

begin_class
DECL|class|QueueComponent
specifier|public
class|class
name|QueueComponent
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
extends|extends
name|SedaComponent
argument_list|<
name|E
argument_list|>
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
name|QueueComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|QueueComponent ()
specifier|public
name|QueueComponent
parameter_list|()
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"This component has been deprecated; please use the seda: URI format instead of queue:"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

