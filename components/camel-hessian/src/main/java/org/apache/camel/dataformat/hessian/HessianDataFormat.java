begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.hessian
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|hessian
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|com
operator|.
name|caucho
operator|.
name|hessian
operator|.
name|io
operator|.
name|Hessian2Input
import|;
end_import

begin_import
import|import
name|com
operator|.
name|caucho
operator|.
name|hessian
operator|.
name|io
operator|.
name|Hessian2Output
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
name|spi
operator|.
name|DataFormat
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
name|DataFormatName
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
name|support
operator|.
name|ServiceSupport
import|;
end_import

begin_comment
comment|/**  * The<a href="http://camel.apache.org/data-format.html">data format</a>  * using<a href="http://hessian.caucho.com/doc/hessian-serialization.html">Hessian Serialization</a>.  *  * @since 2.17  */
end_comment

begin_class
DECL|class|HessianDataFormat
specifier|public
class|class
name|HessianDataFormat
extends|extends
name|ServiceSupport
implements|implements
name|DataFormat
implements|,
name|DataFormatName
block|{
DECL|field|FORMAT_NAME
specifier|private
specifier|static
specifier|final
name|String
name|FORMAT_NAME
init|=
literal|"hessian"
decl_stmt|;
annotation|@
name|Override
DECL|method|getDataFormatName ()
specifier|public
name|String
name|getDataFormatName
parameter_list|()
block|{
return|return
name|FORMAT_NAME
return|;
block|}
annotation|@
name|Override
DECL|method|marshal (final Exchange exchange, final Object graph, final OutputStream outputStream)
specifier|public
name|void
name|marshal
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|Object
name|graph
parameter_list|,
specifier|final
name|OutputStream
name|outputStream
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|Hessian2Output
name|out
init|=
operator|new
name|Hessian2Output
argument_list|(
name|outputStream
argument_list|)
decl_stmt|;
try|try
block|{
name|out
operator|.
name|startMessage
argument_list|()
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|graph
argument_list|)
expr_stmt|;
name|out
operator|.
name|completeMessage
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
try|try
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|unmarshal (final Exchange exchange, final InputStream inputStream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|InputStream
name|inputStream
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|Hessian2Input
name|in
init|=
operator|new
name|Hessian2Input
argument_list|(
name|inputStream
argument_list|)
decl_stmt|;
try|try
block|{
name|in
operator|.
name|startMessage
argument_list|()
expr_stmt|;
specifier|final
name|Object
name|obj
init|=
name|in
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|in
operator|.
name|completeMessage
argument_list|()
expr_stmt|;
return|return
name|obj
return|;
block|}
finally|finally
block|{
try|try
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
end_class

end_unit

