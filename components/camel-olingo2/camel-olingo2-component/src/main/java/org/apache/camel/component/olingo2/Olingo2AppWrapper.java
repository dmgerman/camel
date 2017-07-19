begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.olingo2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|olingo2
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RuntimeCamelException
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
name|component
operator|.
name|olingo2
operator|.
name|api
operator|.
name|Olingo2App
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
name|component
operator|.
name|olingo2
operator|.
name|api
operator|.
name|Olingo2ResponseHandler
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
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|edm
operator|.
name|Edm
import|;
end_import

begin_comment
comment|/**  * Holder class for {@link org.apache.camel.component.olingo2.api.Olingo2App}  * and its lazily read {@link org.apache.olingo.odata2.api.edm.Edm}.  */
end_comment

begin_class
DECL|class|Olingo2AppWrapper
specifier|public
class|class
name|Olingo2AppWrapper
block|{
DECL|field|olingo2App
specifier|private
specifier|final
name|Olingo2App
name|olingo2App
decl_stmt|;
DECL|field|edm
specifier|private
specifier|volatile
name|Edm
name|edm
decl_stmt|;
DECL|method|Olingo2AppWrapper (Olingo2App olingo2App)
specifier|public
name|Olingo2AppWrapper
parameter_list|(
name|Olingo2App
name|olingo2App
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|olingo2App
argument_list|,
literal|"olingo2App"
argument_list|)
expr_stmt|;
name|this
operator|.
name|olingo2App
operator|=
name|olingo2App
expr_stmt|;
block|}
DECL|method|getOlingo2App ()
specifier|public
name|Olingo2App
name|getOlingo2App
parameter_list|()
block|{
return|return
name|olingo2App
return|;
block|}
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
name|olingo2App
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|// double checked locking based singleton Edm reader
DECL|method|getEdm ()
specifier|public
name|Edm
name|getEdm
parameter_list|()
throws|throws
name|RuntimeCamelException
block|{
name|Edm
name|localEdm
init|=
name|edm
decl_stmt|;
if|if
condition|(
name|localEdm
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|localEdm
operator|=
name|edm
expr_stmt|;
if|if
condition|(
name|localEdm
operator|==
literal|null
condition|)
block|{
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|Exception
index|[]
name|error
init|=
operator|new
name|Exception
index|[
literal|1
index|]
decl_stmt|;
name|olingo2App
operator|.
name|read
argument_list|(
literal|null
argument_list|,
literal|"$metadata"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
operator|new
name|Olingo2ResponseHandler
argument_list|<
name|Edm
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onResponse
parameter_list|(
name|Edm
name|response
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|responseHeaders
parameter_list|)
block|{
name|edm
operator|=
name|response
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onException
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|error
index|[
literal|0
index|]
operator|=
name|ex
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onCanceled
parameter_list|()
block|{
name|error
index|[
literal|0
index|]
operator|=
operator|new
name|RuntimeCamelException
argument_list|(
literal|"OData HTTP request cancelled!"
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
try|try
block|{
comment|// wait until response or timeout
name|latch
operator|.
name|await
argument_list|()
expr_stmt|;
specifier|final
name|Exception
name|ex
init|=
name|error
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
name|ex
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|ex
operator|instanceof
name|RuntimeCamelException
condition|)
block|{
throw|throw
operator|(
name|RuntimeCamelException
operator|)
name|ex
throw|;
block|}
else|else
block|{
specifier|final
name|String
name|message
init|=
name|ex
operator|.
name|getMessage
argument_list|()
operator|!=
literal|null
condition|?
name|ex
operator|.
name|getMessage
argument_list|()
else|:
name|ex
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Error reading EDM: "
operator|+
name|message
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|localEdm
operator|=
name|edm
expr_stmt|;
block|}
block|}
block|}
return|return
name|localEdm
return|;
block|}
block|}
end_class

end_unit

