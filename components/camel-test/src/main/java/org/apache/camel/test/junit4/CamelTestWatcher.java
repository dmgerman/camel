begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.junit4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|junit4
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
name|util
operator|.
name|StopWatch
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|TestWatcher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|Description
import|;
end_import

begin_comment
comment|/**  * A JUnit {@link org.junit.rules.TestWatcher} that is used to time how long the test takes.  */
end_comment

begin_class
DECL|class|CamelTestWatcher
specifier|public
class|class
name|CamelTestWatcher
extends|extends
name|TestWatcher
block|{
DECL|field|watch
specifier|private
specifier|final
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|starting (Description description)
specifier|protected
name|void
name|starting
parameter_list|(
name|Description
name|description
parameter_list|)
block|{
name|watch
operator|.
name|restart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|finished (Description description)
specifier|protected
name|void
name|finished
parameter_list|(
name|Description
name|description
parameter_list|)
block|{
name|watch
operator|.
name|taken
argument_list|()
expr_stmt|;
block|}
DECL|method|timeTaken ()
specifier|public
name|long
name|timeTaken
parameter_list|()
block|{
return|return
name|watch
operator|.
name|taken
argument_list|()
return|;
block|}
block|}
end_class

end_unit

