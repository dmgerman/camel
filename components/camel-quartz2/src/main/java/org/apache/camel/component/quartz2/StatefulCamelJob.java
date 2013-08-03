begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quartz2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|quartz2
package|;
end_package

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|DisallowConcurrentExecution
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|PersistJobDataAfterExecution
import|;
end_import

begin_comment
comment|/**  * A stateful job for CamelJob. For Quartz, this means it will re-save all job data map after each job execution,  * and it will not run concurrently within the Quartz thread pool even if you have multiple triggers or misfired  * instruct to do so.  *  * @author Zemian Deng saltnlight5@gmail.com  */
end_comment

begin_class
annotation|@
name|PersistJobDataAfterExecution
annotation|@
name|DisallowConcurrentExecution
DECL|class|StatefulCamelJob
specifier|public
class|class
name|StatefulCamelJob
extends|extends
name|CamelJob
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|25L
decl_stmt|;
block|}
end_class

end_unit

