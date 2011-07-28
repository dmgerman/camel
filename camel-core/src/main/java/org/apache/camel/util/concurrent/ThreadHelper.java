begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.util.concurrent
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|concurrent
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicLong
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

begin_class
DECL|class|ThreadHelper
specifier|public
class|class
name|ThreadHelper
block|{
DECL|field|DEFAULT_PATTERN
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_PATTERN
init|=
literal|"Camel Thread ${counter} - ${name}"
decl_stmt|;
DECL|field|threadCounter
specifier|private
specifier|static
name|AtomicLong
name|threadCounter
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
DECL|method|nextThreadCounter ()
specifier|private
specifier|static
name|long
name|nextThreadCounter
parameter_list|()
block|{
return|return
name|threadCounter
operator|.
name|getAndIncrement
argument_list|()
return|;
block|}
comment|/**      * Creates a new thread name with the given prefix      *      * @param pattern the pattern      * @param name    the name      * @return the thread name, which is unique      */
DECL|method|resolveThreadName (String pattern, String name)
specifier|public
specifier|static
name|String
name|resolveThreadName
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|pattern
operator|==
literal|null
condition|)
block|{
name|pattern
operator|=
name|DEFAULT_PATTERN
expr_stmt|;
block|}
comment|// the name could potential have a $ sign we want to keep
if|if
condition|(
name|name
operator|.
name|indexOf
argument_list|(
literal|"$"
argument_list|)
operator|>
operator|-
literal|1
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|replaceAll
argument_list|(
literal|"\\$"
argument_list|,
literal|"CAMEL_REPLACE_ME"
argument_list|)
expr_stmt|;
block|}
comment|// we support ${longName} and ${name} as name placeholders
name|String
name|longName
init|=
name|name
decl_stmt|;
name|String
name|shortName
init|=
name|name
operator|.
name|contains
argument_list|(
literal|"?"
argument_list|)
condition|?
name|ObjectHelper
operator|.
name|before
argument_list|(
name|name
argument_list|,
literal|"?"
argument_list|)
else|:
name|name
decl_stmt|;
name|String
name|answer
init|=
name|pattern
operator|.
name|replaceFirst
argument_list|(
literal|"\\$\\{counter\\}"
argument_list|,
literal|""
operator|+
name|nextThreadCounter
argument_list|()
argument_list|)
decl_stmt|;
name|answer
operator|=
name|answer
operator|.
name|replaceFirst
argument_list|(
literal|"\\$\\{longName\\}"
argument_list|,
name|longName
argument_list|)
expr_stmt|;
name|answer
operator|=
name|answer
operator|.
name|replaceFirst
argument_list|(
literal|"\\$\\{name\\}"
argument_list|,
name|shortName
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|.
name|indexOf
argument_list|(
literal|"$"
argument_list|)
operator|>
operator|-
literal|1
operator|||
name|answer
operator|.
name|indexOf
argument_list|(
literal|"${"
argument_list|)
operator|>
operator|-
literal|1
operator|||
name|answer
operator|.
name|indexOf
argument_list|(
literal|"}"
argument_list|)
operator|>
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Pattern is invalid: "
operator|+
name|pattern
argument_list|)
throw|;
block|}
if|if
condition|(
name|answer
operator|.
name|indexOf
argument_list|(
literal|"CAMEL_REPLACE_ME"
argument_list|)
operator|>
operator|-
literal|1
condition|)
block|{
name|answer
operator|=
name|answer
operator|.
name|replaceAll
argument_list|(
literal|"CAMEL_REPLACE_ME"
argument_list|,
literal|"\\$"
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

