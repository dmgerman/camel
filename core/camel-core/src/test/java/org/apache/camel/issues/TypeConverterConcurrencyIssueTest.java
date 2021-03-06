begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|issues
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|ContextTestSupport
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
name|NoTypeConversionAvailableException
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
name|impl
operator|.
name|converter
operator|.
name|StaticMethodTypeConverter
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
name|StopWatch
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Testing for CAMEL-5002  */
end_comment

begin_class
DECL|class|TypeConverterConcurrencyIssueTest
specifier|public
class|class
name|TypeConverterConcurrencyIssueTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|size
specifier|private
name|int
name|size
init|=
literal|100
operator|*
literal|1000
decl_stmt|;
annotation|@
name|Test
DECL|method|testTypeConverter ()
specifier|public
name|void
name|testTypeConverter
parameter_list|()
throws|throws
name|Exception
block|{
comment|// add as type converter
name|Method
name|method
init|=
name|TypeConverterConcurrencyIssueTest
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"toMyCamelBean"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|method
argument_list|)
expr_stmt|;
name|context
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|addTypeConverter
argument_list|(
name|MyCamelBean
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|,
operator|new
name|StaticMethodTypeConverter
argument_list|(
name|method
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|ExecutorService
name|pool
init|=
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newThreadPool
argument_list|(
name|this
argument_list|,
literal|"test"
argument_list|,
literal|50
argument_list|,
literal|50
argument_list|)
decl_stmt|;
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
name|size
argument_list|)
decl_stmt|;
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|pool
operator|.
name|submit
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|MyCamelBean
operator|.
name|class
argument_list|,
literal|"1;MyCamel"
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoTypeConversionAvailableException
name|e
parameter_list|)
block|{
comment|// ignore, as the latch will not be decremented anymore
comment|// so that the assert below
comment|// will fail after the one minute timeout anyway
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
literal|"The expected mandatory conversions failed!"
argument_list|,
name|latch
operator|.
name|await
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|MINUTES
argument_list|)
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Took {} millis to convert {} objects"
argument_list|,
name|watch
operator|.
name|taken
argument_list|()
argument_list|,
name|size
argument_list|)
expr_stmt|;
block|}
DECL|method|toMyCamelBean (String body)
specifier|public
specifier|static
name|MyCamelBean
name|toMyCamelBean
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|MyCamelBean
name|bean
init|=
operator|new
name|MyCamelBean
argument_list|()
decl_stmt|;
name|String
index|[]
name|data
init|=
name|body
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
decl_stmt|;
name|bean
operator|.
name|setId
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|data
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setName
argument_list|(
name|data
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
return|return
name|bean
return|;
block|}
block|}
end_class

end_unit

