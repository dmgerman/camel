begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.asn1
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|asn1
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
import|;
end_import

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
name|Iterator
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
name|util
operator|.
name|IOHelper
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
name|bouncycastle
operator|.
name|asn1
operator|.
name|ASN1InputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|ASN1Primitive
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|ASN1GenericIterator
specifier|public
class|class
name|ASN1GenericIterator
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Iterator
argument_list|<
name|T
argument_list|>
implements|,
name|Closeable
block|{
DECL|field|LOGGER
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ASN1GenericIterator
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|asn1InputStream
specifier|private
specifier|volatile
name|ASN1InputStream
name|asn1InputStream
decl_stmt|;
DECL|field|parent
specifier|private
specifier|volatile
name|T
name|parent
decl_stmt|;
DECL|field|clazz
specifier|private
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
decl_stmt|;
DECL|method|ASN1GenericIterator (Class<T> clazz, InputStream inputStream)
specifier|public
name|ASN1GenericIterator
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|InputStream
name|inputStream
parameter_list|)
block|{
if|if
condition|(
name|inputStream
operator|instanceof
name|ASN1InputStream
condition|)
block|{
name|this
operator|.
name|asn1InputStream
operator|=
operator|(
name|ASN1InputStream
operator|)
name|inputStream
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|asn1InputStream
operator|=
operator|new
name|ASN1InputStream
argument_list|(
name|inputStream
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|parent
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|clazz
operator|=
name|clazz
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
name|asn1InputStream
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|boolean
name|availableDataInCurrentEntry
init|=
name|asn1InputStream
operator|.
name|available
argument_list|()
operator|>
literal|0
decl_stmt|;
if|if
condition|(
operator|!
name|availableDataInCurrentEntry
condition|)
block|{
comment|// advance to the next entry.
name|parent
operator|=
name|getNextElement
argument_list|()
expr_stmt|;
if|if
condition|(
name|parent
operator|==
literal|null
condition|)
block|{
name|asn1InputStream
operator|.
name|close
argument_list|()
expr_stmt|;
name|availableDataInCurrentEntry
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|availableDataInCurrentEntry
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|availableDataInCurrentEntry
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|exception
argument_list|)
throw|;
block|}
block|}
DECL|method|getNextElement ()
specifier|private
name|T
name|getNextElement
parameter_list|()
block|{
if|if
condition|(
name|asn1InputStream
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
name|ASN1Primitive
name|current
init|=
name|getNextEntry
argument_list|()
decl_stmt|;
if|if
condition|(
name|current
operator|!=
literal|null
condition|)
block|{
name|T
name|instance
init|=
name|ObjectHelper
operator|.
name|cast
argument_list|(
name|clazz
argument_list|,
name|createGenericTypeObject
argument_list|(
name|current
argument_list|,
name|clazz
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|instance
return|;
block|}
else|else
block|{
name|LOGGER
operator|.
name|trace
argument_list|(
literal|"close asn1InputStream"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|exception
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|exception
argument_list|)
throw|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
DECL|method|createGenericTypeObject (ASN1Primitive current, Class<T> clazz2)
specifier|private
name|Object
name|createGenericTypeObject
parameter_list|(
name|ASN1Primitive
name|current
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz2
parameter_list|)
throws|throws
name|Throwable
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|paramIS
init|=
operator|new
name|Class
index|[
literal|1
index|]
decl_stmt|;
name|paramIS
index|[
literal|0
index|]
operator|=
name|InputStream
operator|.
name|class
expr_stmt|;
name|Method
name|m
init|=
name|clazz
operator|.
name|getDeclaredMethod
argument_list|(
literal|"decode"
argument_list|,
name|paramIS
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|current
operator|.
name|getEncoded
argument_list|()
argument_list|)
decl_stmt|;
name|Object
name|clazzInstance
init|=
name|clazz
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|m
operator|.
name|invoke
argument_list|(
name|clazzInstance
argument_list|,
name|is
argument_list|)
expr_stmt|;
return|return
name|clazzInstance
return|;
block|}
DECL|method|getNextEntry ()
specifier|private
name|ASN1Primitive
name|getNextEntry
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|asn1InputStream
operator|.
name|readObject
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|remove ()
specifier|public
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|next ()
specifier|public
name|T
name|next
parameter_list|()
block|{
if|if
condition|(
name|parent
operator|==
literal|null
condition|)
block|{
name|parent
operator|=
name|getNextElement
argument_list|()
expr_stmt|;
block|}
name|T
name|answer
init|=
name|parent
decl_stmt|;
name|parent
operator|=
literal|null
expr_stmt|;
name|checkNullAnswer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|checkNullAnswer (T answer)
specifier|private
name|void
name|checkNullAnswer
parameter_list|(
name|T
name|answer
parameter_list|)
block|{
if|if
condition|(
name|answer
operator|==
literal|null
operator|&&
name|asn1InputStream
operator|!=
literal|null
condition|)
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|asn1InputStream
argument_list|)
expr_stmt|;
name|asn1InputStream
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|asn1InputStream
argument_list|)
expr_stmt|;
name|asn1InputStream
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

