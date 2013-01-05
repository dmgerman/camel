begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.guice.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|guice
operator|.
name|support
package|;
end_package

begin_comment
comment|/**  * Some helper methods for working with the {@link Closer} interface  *   * @version  */
end_comment

begin_class
DECL|class|Closers
specifier|public
specifier|final
class|class
name|Closers
block|{
DECL|method|Closers ()
specifier|private
name|Closers
parameter_list|()
block|{
comment|//Helper class
block|}
comment|/**      * Closes the given object with the Closer if the object is not null      *       * @param key      *            the Key or String name of the object to be closed      * @param objectToBeClosed      *            the object that is going to be closed      * @param closer      *            the strategy used to close the object      * @param errors      *            the handler of exceptions if they occur      */
DECL|method|close (Object key, Object objectToBeClosed, Closer closer, CloseErrors errors)
specifier|public
specifier|static
name|void
name|close
parameter_list|(
name|Object
name|key
parameter_list|,
name|Object
name|objectToBeClosed
parameter_list|,
name|Closer
name|closer
parameter_list|,
name|CloseErrors
name|errors
parameter_list|)
block|{
if|if
condition|(
name|objectToBeClosed
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|closer
operator|.
name|close
argument_list|(
name|objectToBeClosed
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|errors
operator|.
name|closeError
argument_list|(
name|key
argument_list|,
name|objectToBeClosed
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
name|throwable
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

